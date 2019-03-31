package org.kinecosystem.kinit.viewmodel.spend;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickSendButtonOnSendKinPage;
import org.kinecosystem.kinit.analytics.Events.Analytics.ViewErrorPopupOnSendKinPage;
import org.kinecosystem.kinit.analytics.Events.Analytics.ViewSendKinPage;
import org.kinecosystem.kinit.repository.UserRepository;
import org.kinecosystem.kinit.server.NetworkServices;
import org.kinecosystem.kinit.server.OperationResultCallback;
import org.kinecosystem.kinit.util.Scheduler;
import org.kinecosystem.kinit.view.spend.ContactData;
import org.kinecosystem.kinit.view.spend.Peer2PeerActions;

import javax.inject.Inject;

import static org.kinecosystem.kinit.server.OfferServiceKt.ERROR_TRANSACTION_FAILED;

public class Peer2PeerViewModel {

    private static final long COMPLETE_TRANSACTION_SCREEN_TIMEOUT = 3000;
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableBoolean sendEnabled = new ObservableBoolean(false);
    public ObservableBoolean isClickable = new ObservableBoolean(true);
    public ObservableBoolean sendingTransaction = new ObservableBoolean(false);
    public ObservableBoolean transactionComplete = new ObservableBoolean(false);
    public ObservableInt maxTransferLength = new ObservableInt(1);
    public ObservableInt amount = new ObservableInt(0);
    public OnClickListener amountClickListener = view -> {
        //keep the cursor at the end of the edittext view
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setSelection(editText.getText().length());
        }
    };
    @Inject
    UserRepository userRepository;
    @Inject
    Analytics analytics;
    @Inject
    NetworkServices servicesProvider;
    @Inject
    Scheduler scheduler;
    private String address;
    private Peer2PeerActions actions;
    private ObservableField<Boolean> positiveAmount = new ObservableField<Boolean>(false) {
        @Override
        public void set(Boolean value) {
            super.set(value);
            sendEnabled.set(value && hasValidAddress.get());
        }
    };
    private ObservableField<Boolean> hasValidAddress = new ObservableField<Boolean>(false) {
        @Override
        public void set(Boolean value) {
            super.set(value);
            sendEnabled.set(value && positiveAmount.get());
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
    private int minKinTransfer, maxKinTransfer;

    public Peer2PeerViewModel() {
        KinitApplication.coreComponent.inject(this);
        //max chars of the edit text is base on max transfer amount
        maxKinTransfer = userRepository.getP2pMaxKin();
        minKinTransfer = userRepository.getP2pMinKin();
        maxTransferLength.set(String.valueOf(maxKinTransfer).length());
    }

    public void onResume() {
        analytics.logEvent(new ViewSendKinPage());
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
                    R.string.p2p_back_to_list_action, true);
        }
        logEventPopUp(Analytics.P2P_FRIEND_HAS_NO_ADDRESS);
    }

    private void sendPhone(String phoneNumber) {
        final String formattedNumber = PhoneNumberUtils.stripSeparators(phoneNumber);
        servicesProvider.getOfferService()
                .sendContact(formattedNumber, new OperationResultCallback<String>() {
                    @Override
                    public void onResult(String addressResponse) {
                        if (userRepository.getUserInfo().getPublicAddress().equals(addressResponse)) {
                            address = "";
                            onSelfAddressMatch();
                        } else {
                            address = addressResponse;
                            onGotValidAddress();
                        }
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

    private void onSelfAddressMatch() {
        name.set("");
        hasValidAddress.set(false);
        if (actions != null) {
            actions.showDialog(R.string.p2p_send_kin_to_self_title, R.string.p2p_send_kin_to_self_body,
                    R.string.p2p_back_to_list_action, true);
        }
        logEventPopUp(Analytics.P2P_SEND_KIN_TO_SELF);
    }

    private void onNoAddressMatch() {
        name.set("");
        hasValidAddress.set(false);
        if (actions != null) {
            actions.showDialog(R.string.p2p_friend_has_no_kinit_title, R.string.p2p_friend_has_no_kinit_body,
                    R.string.p2p_back_to_list_action, true);
        }
        logEventPopUp(Analytics.P2P_FRIEND_HAS_NO_ADDRESS);
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
        analytics.logEvent(new ClickSendButtonOnSendKinPage(amount.get()));
        if (isValidAmount()) {
            sendingTransaction.set(true);
            isClickable.set(false);
            if (actions != null) {
                actions.onStartTransaction();
            }
            servicesProvider.getOfferService()
                    .p2pTransfer(null, address, amount.get(), new OperationResultCallback<String>() {
                        @Override
                        public void onResult(String transactionId) {
                            isClickable.set(true);
                            sendingTransaction.set(false);
                            transactionComplete.set(true);
                            if (actions != null) {
                                actions.onTransactionComplete();
                                scheduler.scheduleOnMain(() -> {
                                    if (actions != null) {
                                        actions.closeScreen();
                                    }
                                }, COMPLETE_TRANSACTION_SCREEN_TIMEOUT);
                            }
                        }

                        @Override
                        public void onError(int errorCode) {
                            isClickable.set(true);
                            if (actions != null) {
                                if (errorCode == ERROR_TRANSACTION_FAILED) {
                                    actions.showDialog(R.string.p2p_server_problem_title, R.string.general_problem_body,
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
        analytics.logEvent(new ViewErrorPopupOnSendKinPage(type));
    }

    private boolean isValidAmount() {
        if (amount.get() < minKinTransfer) {
            logEventPopUp(Analytics.P2P_EXCEED_MIN_MAX);
            actions.showDialog(R.string.p2p_amount_too_small_title, R.string.p2p_amount_too_small_body, minKinTransfer,
                    R.string.p2p_amount_not_valid_action, false);
            return false;
        } else if (amount.get() > maxKinTransfer) {
            logEventPopUp(Analytics.P2P_EXCEED_MIN_MAX);
            actions.showDialog(R.string.p2p_amount_too_big_title, R.string.p2p_amount_too_big_body, maxKinTransfer,
                    R.string.p2p_amount_not_valid_action, false);
            return false;
        } else if (amount.get() > servicesProvider.getWalletService().getBalanceInt()) {
            logEventPopUp(Analytics.P2P_NOT_ENOUGH_BALANCE);
            actions
                    .showDialog(R.string.p2p_not_enough_balance_title, R.string.p2p_not_enough_balance_body, minKinTransfer,
                            R.string.p2p_amount_not_valid_action, false);
            return false;
        }
        return true;
    }
}
