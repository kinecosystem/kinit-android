package org.kinecosystem.kinit.viewmodel.spend;

import static org.kinecosystem.kinit.network.OfferServiceKt.ERROR_TRANSACTION_FAILED;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import org.kinecosystem.kinit.CoreComponentsProvider;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickSendButtonOnSendKinPage;
import org.kinecosystem.kinit.analytics.Events.Analytics.ViewErrorPopupOnSendKinPage;
import org.kinecosystem.kinit.analytics.Events.Analytics.ViewSendKinPage;
import org.kinecosystem.kinit.analytics.Events.DialogErrorType;
import org.kinecosystem.kinit.network.OperationResultCallback;
import org.kinecosystem.kinit.view.spend.ContactData;
import org.kinecosystem.kinit.view.spend.Peer2PeerActions;

public class Peer2PeerViewModel {

    private CoreComponentsProvider coreComponents;
    private String address;
    private Peer2PeerActions actions;
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableBoolean sendEnabled = new ObservableBoolean(false);
    public ObservableBoolean sendingTransaction = new ObservableBoolean(false);
    public ObservableBoolean transactionComplete = new ObservableBoolean(false);
    private ObservableField<Boolean> hasValidAddress = new ObservableField<Boolean>(false) {
        @Override
        public void set(Boolean value) {
            super.set(value);
            sendEnabled.set(value && positiveAmount.get());
        }
    };
    private ObservableField<Boolean> positiveAmount = new ObservableField<Boolean>(false) {
        @Override
        public void set(Boolean value) {
            super.set(value);
            sendEnabled.set(value && hasValidAddress.get());
        }
    };
    public ObservableInt maxTransferLength = new ObservableInt(1);
    public ObservableInt amount = new ObservableInt(0);
    private static final long COMPLETE_TRANSACTION_SCREEN_TIMEOUT = 4000;
    private int minKinTransfer, maxKinTransfer;
    public OnClickListener amountClickListener = view -> {
        //keep the cursor at the end of the edittext view
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setSelection(editText.getText().length());
        }
    };

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() >= 2) {
                if (editable.charAt(0) == '0') {
                    editable.delete(0, 1);
                }
            }
            positiveAmount.set(editable.length() > 0 && !editable.toString().startsWith("0"));
            amount.set(editable.length() > 0 ? Integer.valueOf(editable.toString()) : 0);
        }
    };

    public void onResume() {
        coreComponents.analytics().logEvent(new ViewSendKinPage());
    }

    public Peer2PeerViewModel(CoreComponentsProvider coreComponentsProvider) {
        this.coreComponents = coreComponentsProvider;
        //max chars of the edit text is base on max transfer amount
        maxKinTransfer = coreComponents.userRepo().getP2pMaxKin();
        minKinTransfer = coreComponents.userRepo().getP2pMinKin();
        maxTransferLength.set(String.valueOf(maxKinTransfer).length());
    }

    public void setPeer2PeerActions(Peer2PeerActions actions) {
        this.actions = actions;
    }

    public void updateContact(ContactData contactData) {
        name.set(contactData.getName());
        sendPhone(contactData.getPhoneNumber());
    }

    public void onErrorPickContact() {
        name.set("");
        hasValidAddress.set(false);
        if (actions != null) {
            actions.showDialog(R.string.p2p_friend_has_no_kinit_title, R.string.p2p_friend_has_no_kinit_body,
                R.string.p2p_friend_has_no_kinit_action, true);
        }
        logEventPopUp(DialogErrorType.FRIEND_HAS_NO_ADDRESS);
    }

    private void sendPhone(String phoneNumber) {
        final String formattedNumber = PhoneNumberUtils.stripSeparators(phoneNumber);
        coreComponents.services().getOfferService()
            .sendContact(formattedNumber, new OperationResultCallback<String>() {
                @Override
                public void onResult(String addressResponse) {
                    address = addressResponse;
                    onGotValidAddress();
                }

                @Override
                public void onError(int errorCode) {
                    address = "";
                    onNoAddressMatch();
                }
            });

    }

    private void onGotValidAddress() {
        hasValidAddress.set(true);
        if (actions != null) {
            actions.onReadyForTransaction();
        }
    }

    private void onNoAddressMatch() {
        name.set("");
        hasValidAddress.set(false);
        if (actions != null) {
            actions.showDialog(R.string.p2p_friend_has_no_kinit_title, R.string.p2p_friend_has_no_kinit_body,
                R.string.p2p_friend_has_no_kinit_action, true);
        }
        logEventPopUp(DialogErrorType.FRIEND_HAS_NO_ADDRESS);
    }

    public void onCloseButtonClicked(View view) {
        if (actions != null) {
            actions.closeScreen();
        }
    }

    public void onPickContactClicked(View view) {
        if (actions != null) {
            actions.pickContact();
        }
    }

    public void onSend(View view) {
        coreComponents.analytics().logEvent(new ClickSendButtonOnSendKinPage((float) amount.get()));
        if (isValidAmount()) {
            sendingTransaction.set(true);
            if (actions != null) {
                actions.onStartTransaction();
            }
            coreComponents.services().getOfferService()
                .p2pTransfer(address, amount.get(), new OperationResultCallback<String>() {
                    @Override
                    public void onResult(String transactionId) {
                        sendingTransaction.set(false);
                        transactionComplete.set(true);
                        if (actions != null) {
                            actions.onTransactionComplete();
                            coreComponents.scheduler().scheduleOnMain(() -> {
                                if (actions != null) {
                                    actions.closeScreen();
                                }
                            }, COMPLETE_TRANSACTION_SCREEN_TIMEOUT);
                        }
                    }

                    @Override
                    public void onError(int errorCode) {
                        if (actions != null) {
                            if (errorCode == ERROR_TRANSACTION_FAILED) {
                                actions.showDialog(R.string.p2p_server_problem_title, R.string.p2p_server_problem_body,
                                    R.string.dialog_back_to_list, true);
                                sendingTransaction.set(false);
                                transactionComplete.set(false);
                            }
                        }
                    }
                });
        }
    }

    private void logEventPopUp(String type) {
        coreComponents.analytics().logEvent(new ViewErrorPopupOnSendKinPage(type));
    }

    private boolean isValidAmount() {
        if (amount.get() < minKinTransfer) {
            logEventPopUp(DialogErrorType.EXCEED_MIN_MAX);
            actions.showDialog(R.string.p2p_amount_too_small_title, R.string.p2p_amount_too_small_body, minKinTransfer,
                R.string.p2p_amount_not_valid_action, false);
            return false;
        } else if (amount.get() > maxKinTransfer) {
            logEventPopUp(DialogErrorType.EXCEED_MIN_MAX);
            actions.showDialog(R.string.p2p_amount_too_big_title, R.string.p2p_amount_too_big_body, maxKinTransfer,
                R.string.p2p_amount_not_valid_action, false);
            return false;
        } else if (amount.get() > coreComponents.services().getWalletService().getBalanceInt()) {
            logEventPopUp(DialogErrorType.NOT_ENOUGH_BALANCE);
            actions
                .showDialog(R.string.p2p_not_enough_balance_title, R.string.p2p_not_enough_balance_body, minKinTransfer,
                    R.string.p2p_amount_not_valid_action, false);
            return false;
        }
        return true;
    }
}
