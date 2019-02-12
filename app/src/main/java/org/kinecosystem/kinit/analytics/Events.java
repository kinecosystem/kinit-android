package org.kinecosystem.kinit.analytics;//
// Events.java
//
// Don't edit this file.
// Generated at 2019-02-05 15:02:11 +0000 by Kik BI-Generator.
//

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Events {

    public interface Event {
        String getName();
        JSONObject getProperties();
    }

    public static class UserProperties {
        public static final String BALANCE = "balance";
        public static final String EARN_COUNT = "earn_count";
        public static final String PUSH_ENABLED = "push_enabled";
        public static final String REFERRAL_SOURCE = "referral_source";
        public static final String SPEND_COUNT = "spend_count";
        public static final String TOTAL_KIN_EARNED = "total_KIN_earned";
        public static final String TOTAL_KIN_SPENT = "total_KIN_spent";
        public static final String TRANSACTION_COUNT = "transaction_count";
    }


    public static class Business {

        /**
         user completes a task (e.g. answered all questionnaire's Qs). Event name: `earning_task_completed`
         */
        public static class EarningTaskCompleted implements Event {

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private String taskCategory;

            private String taskId;

            private String taskTitle;

            private String taskType;



            public EarningTaskCompleted( String creator, Float estimatedTimeToComplete, Integer kinReward, String taskCategory, String taskId, String taskTitle, String taskType) {

                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
                this.taskType = taskType;
            }

            @Override
            public String getName() {
                return "earning_task_completed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);
                    properties.put("task_type", taskType);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user starts a task to earn KIN. Event name: `earning_task_started`
         */
        public static class EarningTaskStarted implements Event {

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private String taskCategory;

            private String taskId;

            private String taskTitle;

            private String taskType;



            public EarningTaskStarted( String creator, Float estimatedTimeToComplete, Integer kinReward, String taskCategory, String taskId, String taskTitle, String taskType) {

                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
                this.taskType = taskType;
            }

            @Override
            public String getName() {
                return "earning_task_started";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);
                    properties.put("task_type", taskType);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         KIN transaction failure. Event name: `KIN_transaction_failed`
         */
        public static class KINTransactionFailed implements Event {

            private String failureReason;

            private Integer kinAmount;

            private String transactionType;



            public KINTransactionFailed( String failureReason, Integer kinAmount, String transactionType) {

                this.failureReason = failureReason;
                this.kinAmount = kinAmount;
                this.transactionType = transactionType;
            }

            @Override
            public String getName() {
                return "KIN_transaction_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);
                    properties.put("KIN_amount", kinAmount);
                    properties.put("transaction_type", transactionType);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         successful KIN transaction (send / receive). Event name: `KIN_transaction_succeeded`
         */
        public static class KINTransactionSucceeded implements Event {

            private Integer kinAmount;

            private String transactionId;

            private String transactionType;



            public KINTransactionSucceeded( Integer kinAmount, String transactionId, String transactionType) {

                this.kinAmount = kinAmount;
                this.transactionId = transactionId;
                this.transactionType = transactionType;
            }

            @Override
            public String getName() {
                return "KIN_transaction_succeeded";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("KIN_amount", kinAmount);
                    properties.put("transaction_id", transactionId);
                    properties.put("transaction_type", transactionType);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user receives the spending offer he purchased (e.g. coupon code) . Event name: `spending_offer_provided`
         */
        public static class SpendingOfferProvided implements Event {

            private String brandName;

            private Integer kinPrice;

            private String offerCategory;

            private String offerId;

            private String offerName;

            private String offerType;



            public SpendingOfferProvided( String brandName, Integer kinPrice, String offerCategory, String offerId, String offerName, String offerType) {

                this.brandName = brandName;
                this.kinPrice = kinPrice;
                this.offerCategory = offerCategory;
                this.offerId = offerId;
                this.offerName = offerName;
                this.offerType = offerType;
            }

            @Override
            public String getName() {
                return "spending_offer_provided";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("brand_name", brandName);
                    properties.put("KIN_price", kinPrice);
                    properties.put("offer_category", offerCategory);
                    properties.put("offer_id", offerId);
                    properties.put("offer_name", offerName);
                    properties.put("offer_type", offerType);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user purchases a spending offer. Event name: `spending_offer_requested`
         */
        public static class SpendingOfferRequested implements Event {

            private String brandName;

            private Integer kinPrice;

            private String offerCategory;

            private String offerId;

            private String offerName;

            private String offerType;



            public SpendingOfferRequested( String brandName, Integer kinPrice, String offerCategory, String offerId, String offerName, String offerType) {

                this.brandName = brandName;
                this.kinPrice = kinPrice;
                this.offerCategory = offerCategory;
                this.offerId = offerId;
                this.offerName = offerName;
                this.offerType = offerType;
            }

            @Override
            public String getName() {
                return "spending_offer_requested";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("brand_name", brandName);
                    properties.put("KIN_price", kinPrice);
                    properties.put("offer_category", offerCategory);
                    properties.put("offer_id", offerId);
                    properties.put("offer_name", offerName);
                    properties.put("offer_type", offerType);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user submits a support request. Event name: `support_request_sent`
         */
        public static class SupportRequestSent implements Event {

            private String faqCategory;

            private String faqSubcategory;



            public SupportRequestSent( String faqCategory, String faqSubcategory) {

                this.faqCategory = faqCategory;
                this.faqSubcategory = faqSubcategory;
            }

            @Override
            public String getName() {
                return "support_request_sent";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("FAQ_category", faqCategory);
                    properties.put("FAQ_subcategory", faqSubcategory);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user was successfully created in the server (user ID). Event name: `user_registered`
         */
        public static class UserRegistered implements Event {




            @Override
            public String getName() {
                return "user_registered";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         Stellar wallet (account) successfully created for the user. Event name: `wallet_created`
         */
        public static class WalletCreated implements Event {




            @Override
            public String getName() {
                return "wallet_created";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user was successfully verified (completed phone verification). Event name: `user_verified`
         */
        public static class UserVerified implements Event {




            @Override
            public String getName() {
                return "user_verified";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user successfully completed the wallet backup process. Event name: `wallet_backed_up`
         */
        public static class WalletBackedUp implements Event {




            @Override
            public String getName() {
                return "wallet_backed_up";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user successfully restored his/her wallet. Event name: `wallet_restored`
         */
        public static class WalletRestored implements Event {




            @Override
            public String getName() {
                return "wallet_restored";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user submits a feedback email. Event name: `feedback_sent`
         */
        public static class FeedbackSent implements Event {




            @Override
            public String getName() {
                return "feedback_sent";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user successfully sent Kin to another app. Event name: `cross_app_KIN_sent`
         */
        public static class CrossAppKinSent implements Event {

            private String appCategory;

            private String appId;

            private String appName;

            private Integer kinAmount;



            public CrossAppKinSent( String appCategory, String appId, String appName, Integer kinAmount) {

                this.appCategory = appCategory;
                this.appId = appId;
                this.appName = appName;
                this.kinAmount = kinAmount;
            }

            @Override
            public String getName() {
                return "cross_app_KIN_sent";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("app_category", appCategory);
                    properties.put("app_id", appId);
                    properties.put("app_name", appName);
                    properties.put("KIN_amount", kinAmount);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User successfully submitted a Feedback form. Event name: `feedbackform_sent`
         */
        public static class FeedbackformSent implements Event {




            @Override
            public String getName() {
                return "feedbackform_sent";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User failed to send Kin to another app. Event name: `cross_app_KIN_failure`
         */
        public static class CrossAppKinFailure implements Event {

            private String failureReason;

            private String failureType;



            public CrossAppKinFailure( String failureReason, String failureType) {

                this.failureReason = failureReason;
                this.failureType = failureType;
            }

            @Override
            public String getName() {
                return "cross_app_KIN_failure";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);
                    properties.put("failure_type", failureType);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User submitted at least 5 topics he/she likes. Event name: `topics_chosen`
         */
        public static class TopicsChosen implements Event {

            private String topic;



            public TopicsChosen( String topic) {

                this.topic = topic;
            }

            @Override
            public String getName() {
                return "topics_chosen";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("topic", topic);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User failed to back up wallet. Event name: `wallet_backup_failed`
         */
        public static class WalletBackupFailed implements Event {

            private String failureReason;



            public WalletBackupFailed( String failureReason) {

                this.failureReason = failureReason;
            }

            @Override
            public String getName() {
                return "wallet_backup_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User failed to restore wallet. Event name: `wallet_restore_failed`
         */
        public static class WalletRestoreFailed implements Event {

            private String failureReason;



            public WalletRestoreFailed( String failureReason) {

                this.failureReason = failureReason;
            }

            @Override
            public String getName() {
                return "wallet_restore_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);

                    properties.put("event_type", "business");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

    }

    public static class Analytics {

        /**
         user views splash screen (=app launch). Event name: `view_Splashscreen_page`
         */
        public static class ViewSplashscreenPage implements Event {




            @Override
            public String getName() {
                return "view_Splashscreen_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Splashscreen");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks an item on the navigation menu. Event name: `click_Menu_item`
         */
        public static class ClickMenuItem implements Event {

            private String menuItemName;



            public ClickMenuItem( String menuItemName) {

                this.menuItemName = menuItemName;
            }

            @Override
            public String getName() {
                return "click_Menu_item";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("menu_item_name", menuItemName);

                    properties.put("item_name", "Menu");
                    properties.put("item_type", "item");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user answers a question, as part of a questionnaire. Event name: `click_Answer_button_on_Question_page`
         */
        public static class ClickAnswerButtonOnQuestionPage implements Event {

            private String answerId;

            private Integer answerOrder;

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private Integer numberOfAnswers;

            private Integer numberOfQuestions;

            private String questionId;

            private Integer questionOrder;

            private String questionType;

            private String taskCategory;

            private String taskId;

            private String taskTitle;



            public ClickAnswerButtonOnQuestionPage( String answerId, Integer answerOrder, String creator, Float estimatedTimeToComplete, Integer kinReward, Integer numberOfAnswers, Integer numberOfQuestions, String questionId, Integer questionOrder, String questionType, String taskCategory, String taskId, String taskTitle) {

                this.answerId = answerId;
                this.answerOrder = answerOrder;
                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.numberOfAnswers = numberOfAnswers;
                this.numberOfQuestions = numberOfQuestions;
                this.questionId = questionId;
                this.questionOrder = questionOrder;
                this.questionType = questionType;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
            }

            @Override
            public String getName() {
                return "click_Answer_button_on_Question_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("answer_id", answerId);
                    properties.put("answer_order", answerOrder);
                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("number_of_answers", numberOfAnswers);
                    properties.put("number_of_questions", numberOfQuestions);
                    properties.put("question_id", questionId);
                    properties.put("question_order", questionOrder);
                    properties.put("question_type", questionType);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);

                    properties.put("item_name", "Answer");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Question");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user closes a question page, as part of a questionnaire. Event name: `click_Close_button_on_Question_page`
         */
        public static class ClickCloseButtonOnQuestionPage implements Event {

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private Integer numberOfAnswers;

            private Integer numberOfQuestions;

            private String questionId;

            private Integer questionOrder;

            private String questionType;

            private String taskCategory;

            private String taskId;

            private String taskTitle;



            public ClickCloseButtonOnQuestionPage( String creator, Float estimatedTimeToComplete, Integer kinReward, Integer numberOfAnswers, Integer numberOfQuestions, String questionId, Integer questionOrder, String questionType, String taskCategory, String taskId, String taskTitle) {

                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.numberOfAnswers = numberOfAnswers;
                this.numberOfQuestions = numberOfQuestions;
                this.questionId = questionId;
                this.questionOrder = questionOrder;
                this.questionType = questionType;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
            }

            @Override
            public String getName() {
                return "click_Close_button_on_Question_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("number_of_answers", numberOfAnswers);
                    properties.put("number_of_questions", numberOfQuestions);
                    properties.put("question_id", questionId);
                    properties.put("question_order", questionOrder);
                    properties.put("question_type", questionType);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);

                    properties.put("item_name", "Close");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Question");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user closes the earning task end page . Event name: `click_Close_button_on_Reward_page`
         */
        public static class ClickCloseButtonOnRewardPage implements Event {

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private String taskCategory;

            private String taskId;

            private String taskTitle;

            private String taskType;



            public ClickCloseButtonOnRewardPage( String creator, Float estimatedTimeToComplete, Integer kinReward, String taskCategory, String taskId, String taskTitle, String taskType) {

                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
                this.taskType = taskType;
            }

            @Override
            public String getName() {
                return "click_Close_button_on_Reward_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);
                    properties.put("task_type", taskType);

                    properties.put("item_name", "Close");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Reward");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks on button to start an earning task. Event name: `click_Start_button_on_Task_page`
         */
        public static class ClickStartButtonOnTaskPage implements Event {

            private Boolean alreadyStarted;

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private String taskCategory;

            private String taskId;

            private String taskTitle;

            private String taskType;



            public ClickStartButtonOnTaskPage( Boolean alreadyStarted, String creator, Float estimatedTimeToComplete, Integer kinReward, String taskCategory, String taskId, String taskTitle, String taskType) {

                this.alreadyStarted = alreadyStarted;
                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
                this.taskType = taskType;
            }

            @Override
            public String getName() {
                return "click_Start_button_on_Task_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("already_started", alreadyStarted);
                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);
                    properties.put("task_type", taskType);

                    properties.put("item_name", "Start");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Task");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the animation after KIN was successfully provided. Event name: `view_KIN_Provided_image_on_Reward_page`
         */
        public static class ViewKinProvidedImageOnRewardPage implements Event {

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private String taskCategory;

            private String taskId;

            private String taskTitle;

            private String taskType;



            public ViewKinProvidedImageOnRewardPage( String creator, Float estimatedTimeToComplete, Integer kinReward, String taskCategory, String taskId, String taskTitle, String taskType) {

                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
                this.taskType = taskType;
            }

            @Override
            public String getName() {
                return "view_KIN_Provided_image_on_Reward_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);
                    properties.put("task_type", taskType);

                    properties.put("item_name", "KIN_Provided");
                    properties.put("item_type", "image");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Reward");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the next task availability . Event name: `view_Locked_Task_page`
         */
        public static class ViewLockedTaskPage implements Event {

            private Integer timeToUnlock;



            public ViewLockedTaskPage( Integer timeToUnlock) {

                this.timeToUnlock = timeToUnlock;
            }

            @Override
            public String getName() {
                return "view_Locked_Task_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("time_to_unlock", timeToUnlock);

                    properties.put("item_name", "Locked_Task");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views question page, as part of a questionnaire. Event name: `view_Question_page`
         */
        public static class ViewQuestionPage implements Event {

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private Integer numberOfQuestions;

            private String questionId;

            private Integer questionOrder;

            private String questionType;

            private String taskCategory;

            private String taskId;

            private String taskTitle;



            public ViewQuestionPage( String creator, Float estimatedTimeToComplete, Integer kinReward, Integer numberOfQuestions, String questionId, Integer questionOrder, String questionType, String taskCategory, String taskId, String taskTitle) {

                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.numberOfQuestions = numberOfQuestions;
                this.questionId = questionId;
                this.questionOrder = questionOrder;
                this.questionType = questionType;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
            }

            @Override
            public String getName() {
                return "view_Question_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("number_of_questions", numberOfQuestions);
                    properties.put("question_id", questionId);
                    properties.put("question_order", questionOrder);
                    properties.put("question_type", questionType);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);

                    properties.put("item_name", "Question");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views Reward page after completing a task. Event name: `view_Reward_page`
         */
        public static class ViewRewardPage implements Event {

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private String taskCategory;

            private String taskId;

            private String taskTitle;

            private String taskType;



            public ViewRewardPage( String creator, Float estimatedTimeToComplete, Integer kinReward, String taskCategory, String taskId, String taskTitle, String taskType) {

                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
                this.taskType = taskType;
            }

            @Override
            public String getName() {
                return "view_Reward_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);
                    properties.put("task_type", taskType);

                    properties.put("item_name", "Reward");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views earning task end page (Yay!). Event name: `view_Task_End_page`
         */
        public static class ViewTaskEndPage implements Event {

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private String taskCategory;

            private String taskId;

            private String taskTitle;

            private String taskType;



            public ViewTaskEndPage( String creator, Float estimatedTimeToComplete, Integer kinReward, String taskCategory, String taskId, String taskTitle, String taskType) {

                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
                this.taskType = taskType;
            }

            @Override
            public String getName() {
                return "view_Task_End_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);
                    properties.put("task_type", taskType);

                    properties.put("item_name", "Task_End");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views earning task info (intro) page . Event name: `view_Task_page`
         */
        public static class ViewTaskPage implements Event {

            private String creator;

            private Float estimatedTimeToComplete;

            private Integer kinReward;

            private String taskCategory;

            private String taskId;

            private String taskTitle;

            private String taskType;



            public ViewTaskPage( String creator, Float estimatedTimeToComplete, Integer kinReward, String taskCategory, String taskId, String taskTitle, String taskType) {

                this.creator = creator;
                this.estimatedTimeToComplete = estimatedTimeToComplete;
                this.kinReward = kinReward;
                this.taskCategory = taskCategory;
                this.taskId = taskId;
                this.taskTitle = taskTitle;
                this.taskType = taskType;
            }

            @Override
            public String getName() {
                return "view_Task_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("creator", creator);
                    properties.put("estimated_time_to_complete", estimatedTimeToComplete);
                    properties.put("KIN_reward", kinReward);
                    properties.put("task_category", taskCategory);
                    properties.put("task_id", taskId);
                    properties.put("task_title", taskTitle);
                    properties.put("task_type", taskType);

                    properties.put("item_name", "Task");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks on spending offer item on Spend page . Event name: `click_Offer_item_on_Spend_page`
         */
        public static class ClickOfferItemOnSpendPage implements Event {

            private String brandName;

            private Integer kinPrice;

            private Integer numberOfOffers;

            private String offerCategory;

            private String offerId;

            private String offerName;

            private Integer offerOrder;

            private String offerType;



            public ClickOfferItemOnSpendPage( String brandName, Integer kinPrice, Integer numberOfOffers, String offerCategory, String offerId, String offerName, Integer offerOrder, String offerType) {

                this.brandName = brandName;
                this.kinPrice = kinPrice;
                this.numberOfOffers = numberOfOffers;
                this.offerCategory = offerCategory;
                this.offerId = offerId;
                this.offerName = offerName;
                this.offerOrder = offerOrder;
                this.offerType = offerType;
            }

            @Override
            public String getName() {
                return "click_Offer_item_on_Spend_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("brand_name", brandName);
                    properties.put("KIN_price", kinPrice);
                    properties.put("number_of_offers", numberOfOffers);
                    properties.put("offer_category", offerCategory);
                    properties.put("offer_id", offerId);
                    properties.put("offer_name", offerName);
                    properties.put("offer_order", offerOrder);
                    properties.put("offer_type", offerType);

                    properties.put("item_name", "Offer");
                    properties.put("item_type", "item");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Spend");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks to share/save a coupon code. Event name: `click_Share_button_on_Offer_page`
         */
        public static class ClickShareButtonOnOfferPage implements Event {

            private String brandName;

            private Integer kinPrice;

            private String offerCategory;

            private String offerId;

            private String offerName;

            private String offerType;



            public ClickShareButtonOnOfferPage( String brandName, Integer kinPrice, String offerCategory, String offerId, String offerName, String offerType) {

                this.brandName = brandName;
                this.kinPrice = kinPrice;
                this.offerCategory = offerCategory;
                this.offerId = offerId;
                this.offerName = offerName;
                this.offerType = offerType;
            }

            @Override
            public String getName() {
                return "click_Share_button_on_Offer_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("brand_name", brandName);
                    properties.put("KIN_price", kinPrice);
                    properties.put("offer_category", offerCategory);
                    properties.put("offer_id", offerId);
                    properties.put("offer_name", offerName);
                    properties.put("offer_type", offerType);

                    properties.put("item_name", "Share");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Offer");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the coupon code after purchasing . Event name: `view_Code_text_on_Offer_page`
         */
        public static class ViewCodeTextOnOfferPage implements Event {

            private String brandName;

            private Integer kinPrice;

            private String offerCategory;

            private String offerId;

            private String offerName;

            private String offerType;



            public ViewCodeTextOnOfferPage( String brandName, Integer kinPrice, String offerCategory, String offerId, String offerName, String offerType) {

                this.brandName = brandName;
                this.kinPrice = kinPrice;
                this.offerCategory = offerCategory;
                this.offerId = offerId;
                this.offerName = offerName;
                this.offerType = offerType;
            }

            @Override
            public String getName() {
                return "view_Code_text_on_Offer_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("brand_name", brandName);
                    properties.put("KIN_price", kinPrice);
                    properties.put("offer_category", offerCategory);
                    properties.put("offer_id", offerId);
                    properties.put("offer_name", offerName);
                    properties.put("offer_type", offerType);

                    properties.put("item_name", "Code");
                    properties.put("item_type", "text");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Offer");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views offer details page. Event name: `view_Offer_page`
         */
        public static class ViewOfferPage implements Event {

            private String brandName;

            private Integer kinPrice;

            private String offerCategory;

            private String offerId;

            private String offerName;

            private String offerType;



            public ViewOfferPage( String brandName, Integer kinPrice, String offerCategory, String offerId, String offerName, String offerType) {

                this.brandName = brandName;
                this.kinPrice = kinPrice;
                this.offerCategory = offerCategory;
                this.offerId = offerId;
                this.offerName = offerName;
                this.offerType = offerType;
            }

            @Override
            public String getName() {
                return "view_Offer_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("brand_name", brandName);
                    properties.put("KIN_price", kinPrice);
                    properties.put("offer_category", offerCategory);
                    properties.put("offer_id", offerId);
                    properties.put("offer_name", offerName);
                    properties.put("offer_type", offerType);

                    properties.put("item_name", "Offer");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views Spend page, with spending offers. Event name: `view_Spend_page`
         */
        public static class ViewSpendPage implements Event {

            private Integer numberOfOffers;



            public ViewSpendPage( Integer numberOfOffers) {

                this.numberOfOffers = numberOfOffers;
            }

            @Override
            public String getName() {
                return "view_Spend_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("number_of_offers", numberOfOffers);

                    properties.put("item_name", "Spend");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views Explore page, with live ecosystem apps . Event name: `view_Explore_page`
         */
        public static class ViewExplorePage implements Event {




            @Override
            public String getName() {
                return "view_Explore_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Explore");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views app details page. Event name: `view_App_page`
         */
        public static class ViewAppPage implements Event {

            private String appCategory;

            private String appId;

            private String appName;

            private Boolean transferReady;



            public ViewAppPage( String appCategory, String appId, String appName, Boolean transferReady) {

                this.appCategory = appCategory;
                this.appId = appId;
                this.appName = appName;
                this.transferReady = transferReady;
            }

            @Override
            public String getName() {
                return "view_App_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("app_category", appCategory);
                    properties.put("app_id", appId);
                    properties.put("app_name", appName);
                    properties.put("transfer_ready", transferReady);

                    properties.put("item_name", "App");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks to send Kin to specific app in the ecosystem, a specific app page. Event name: `click_Send_button_on_App_page`
         */
        public static class ClickSendButtonOnAppPage implements Event {

            private String appCategory;

            private String appId;

            private String appName;

            private Boolean transferReady;



            public ClickSendButtonOnAppPage( String appCategory, String appId, String appName, Boolean transferReady) {

                this.appCategory = appCategory;
                this.appId = appId;
                this.appName = appName;
                this.transferReady = transferReady;
            }

            @Override
            public String getName() {
                return "click_Send_button_on_App_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("app_category", appCategory);
                    properties.put("app_id", appId);
                    properties.put("app_name", appName);
                    properties.put("transfer_ready", transferReady);

                    properties.put("item_name", "Send");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "App");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks to send Kin to specific app in the ecosystem, from the app item (discovery). Event name: `click_Send_button_on_App_item`
         */
        public static class ClickSendButtonOnAppItem implements Event {

            private String appCategory;

            private String appId;

            private String appName;



            public ClickSendButtonOnAppItem( String appCategory, String appId, String appName) {

                this.appCategory = appCategory;
                this.appId = appId;
                this.appName = appName;
            }

            @Override
            public String getName() {
                return "click_Send_button_on_App_item";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("app_category", appCategory);
                    properties.put("app_id", appId);
                    properties.put("app_name", appName);

                    properties.put("item_name", "Send");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "App");
                    properties.put("parent_type", "item");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks on the discovery page to get the app from the app item (discovery). Event name: `click_Get_button_on_App_item`
         */
        public static class ClickGetButtonOnAppItem implements Event {

            private String appCategory;

            private String appId;

            private String appName;



            public ClickGetButtonOnAppItem( String appCategory, String appId, String appName) {

                this.appCategory = appCategory;
                this.appId = appId;
                this.appName = appName;
            }

            @Override
            public String getName() {
                return "click_Get_button_on_App_item";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("app_category", appCategory);
                    properties.put("app_id", appId);
                    properties.put("app_name", appName);

                    properties.put("item_name", "Get");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "App");
                    properties.put("parent_type", "item");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks to get the app from a specific app page. Event name: `click_Get_button_on_App_page`
         */
        public static class ClickGetButtonOnAppPage implements Event {

            private String appCategory;

            private String appId;

            private String appName;

            private Boolean transferReady;



            public ClickGetButtonOnAppPage( String appCategory, String appId, String appName, Boolean transferReady) {

                this.appCategory = appCategory;
                this.appId = appId;
                this.appName = appName;
                this.transferReady = transferReady;
            }

            @Override
            public String getName() {
                return "click_Get_button_on_App_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("app_category", appCategory);
                    properties.put("app_id", appId);
                    properties.put("app_name", appName);
                    properties.put("transfer_ready", transferReady);

                    properties.put("item_name", "Get");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "App");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the balance page  . Event name: `view_Balance_page`
         */
        public static class ViewBalancePage implements Event {




            @Override
            public String getName() {
                return "view_Balance_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Balance");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the profile page . Event name: `view_Profile_page`
         */
        public static class ViewProfilePage implements Event {




            @Override
            public String getName() {
                return "view_Profile_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Profile");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         users clicks on support button (opens email), on specific FAQ page. Event name: `click_Support_button`
         */
        public static class ClickSupportButton implements Event {

            private String faqCategory;

            private String faqSubcategory;



            public ClickSupportButton( String faqCategory, String faqSubcategory) {

                this.faqCategory = faqCategory;
                this.faqSubcategory = faqSubcategory;
            }

            @Override
            public String getName() {
                return "click_Support_button";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("FAQ_category", faqCategory);
                    properties.put("FAQ_subcategory", faqSubcategory);

                    properties.put("item_name", "Support");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks push notification to engage with the app. Event name: `click_Engagement_push`
         */
        public static class ClickEngagementPush implements Event {

            private String pushId;

            private String pushText;



            public ClickEngagementPush( String pushId, String pushText) {

                this.pushId = pushId;
                this.pushText = pushText;
            }

            @Override
            public String getName() {
                return "click_Engagement_push";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("push_id", pushId);
                    properties.put("push_text", pushText);

                    properties.put("item_name", "Engagement");
                    properties.put("item_type", "push");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         for iOS only. user clicks the reminder button on locked task page to trigger the push notification approval popup. Event name: `click_Reminder_button_on_Locked_Task_page`
         */
        public static class ClickReminderButtonOnLockedTaskPage implements Event {




            @Override
            public String getName() {
                return "click_Reminder_button_on_Locked_Task_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Reminder");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Locked_Task");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views push notification to engage with the app. Event name: `view_Engagement_push`
         */
        public static class ViewEngagementPush implements Event {

            private String pushId;

            private String pushText;



            public ViewEngagementPush( String pushId, String pushText) {

                this.pushId = pushId;
                this.pushText = pushText;
            }

            @Override
            public String getName() {
                return "view_Engagement_push";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("push_id", pushId);
                    properties.put("push_text", pushText);

                    properties.put("item_name", "Engagement");
                    properties.put("item_type", "push");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views any of the error pages: onboarding, reward, submission, connection. Event name: `view_Error_page`
         */
        public static class ViewErrorPage implements Event {

            private String errorType;

            private String failureReason;



            public ViewErrorPage( String errorType, String failureReason) {

                this.errorType = errorType;
                this.failureReason = failureReason;
            }

            @Override
            public String getName() {
                return "view_Error_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("error_type", errorType);
                    properties.put("failure_reason", failureReason);

                    properties.put("item_name", "Error");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks Retry button on onboarding error page. Event name: `click_Retry_button_on_Error_page`
         */
        public static class ClickRetryButtonOnErrorPage implements Event {

            private String errorType;



            public ClickRetryButtonOnErrorPage( String errorType) {

                this.errorType = errorType;
            }

            @Override
            public String getName() {
                return "click_Retry_button_on_Error_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("error_type", errorType);

                    properties.put("item_name", "Retry");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Error");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the close button on submission / reward errors. Event name: `click_Close_button_on_Error_page`
         */
        public static class ClickCloseButtonOnErrorPage implements Event {

            private String errorType;



            public ClickCloseButtonOnErrorPage( String errorType) {

                this.errorType = errorType;
            }

            @Override
            public String getName() {
                return "click_Close_button_on_Error_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("error_type", errorType);

                    properties.put("item_name", "Close");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Error");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views empty state for no earn tasks / spend offers. Event name: `view_Empty_State_page`
         */
        public static class ViewEmptyStatePage implements Event {

            private String menuItemName;

            private String taskCategory;



            public ViewEmptyStatePage( String menuItemName, String taskCategory) {

                this.menuItemName = menuItemName;
                this.taskCategory = taskCategory;
            }

            @Override
            public String getName() {
                return "view_Empty_State_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("menu_item_name", menuItemName);
                    properties.put("task_category", taskCategory);

                    properties.put("item_name", "Empty_State");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the link on onboarding error, to open support email. Event name: `click_Contact_link_on_Error_page`
         */
        public static class ClickContactLinkOnErrorPage implements Event {

            private String errorType;



            public ClickContactLinkOnErrorPage( String errorType) {

                this.errorType = errorType;
            }

            @Override
            public String getName() {
                return "click_Contact_link_on_Error_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("error_type", errorType);

                    properties.put("item_name", "Contact");
                    properties.put("item_type", "link");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Error");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views error popup when trying to buy an offer. Event name: `view_Error_popup_on_Offer_page`
         */
        public static class ViewErrorPopupOnOfferPage implements Event {

            private String errorType;



            public ViewErrorPopupOnOfferPage( String errorType) {

                this.errorType = errorType;
            }

            @Override
            public String getName() {
                return "view_Error_popup_on_Offer_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("error_type", errorType);

                    properties.put("item_name", "Error");
                    properties.put("item_type", "popup");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Offer");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the button on the error popup (OK / Back to list). Event name: `click_OK_button_on_Error_popup`
         */
        public static class ClickOkButtonOnErrorPopup implements Event {

            private String errorType;



            public ClickOkButtonOnErrorPopup( String errorType) {

                this.errorType = errorType;
            }

            @Override
            public String getName() {
                return "click_OK_button_on_Error_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("error_type", errorType);

                    properties.put("item_name", "OK");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Error");
                    properties.put("parent_type", "popup");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the button on the onboarding page (tutorial pages). Event name: `click_Start_button_on_Onboarding_page`
         */
        public static class ClickStartButtonOnOnboardingPage implements Event {

            private Integer onboardingTutorialPage;



            public ClickStartButtonOnOnboardingPage( Integer onboardingTutorialPage) {

                this.onboardingTutorialPage = onboardingTutorialPage;
            }

            @Override
            public String getName() {
                return "click_Start_button_on_Onboarding_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("onboarding_tutorial_page", onboardingTutorialPage);

                    properties.put("item_name", "Start");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Onboarding");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the onboarding page (tutorial pages). sent also when moving to other tutorial slide. Event name: `view_Onboarding_page`
         */
        public static class ViewOnboardingPage implements Event {

            private Integer onboardingTutorialPage;



            public ViewOnboardingPage( Integer onboardingTutorialPage) {

                this.onboardingTutorialPage = onboardingTutorialPage;
            }

            @Override
            public String getName() {
                return "view_Onboarding_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("onboarding_tutorial_page", onboardingTutorialPage);

                    properties.put("item_name", "Onboarding");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the phone authentication page when phone number should be inserted. Event name: `view_Phone_Auth_page`
         */
        public static class ViewPhoneAuthPage implements Event {




            @Override
            public String getName() {
                return "view_Phone_Auth_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Phone_Auth");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user click the button to continue to verification page. Event name: `click_Next_button_on_Phone_Auth_page`
         */
        public static class ClickNextButtonOnPhoneAuthPage implements Event {




            @Override
            public String getName() {
                return "click_Next_button_on_Phone_Auth_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Next");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Phone_Auth");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the verification page, where a verification code should be inserted. Event name: `view_Verification_page`
         */
        public static class ViewVerificationPage implements Event {




            @Override
            public String getName() {
                return "view_Verification_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Verification");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user gets an error message when entering a wrong verification code. Event name: `view_Error_message_on_Verification_page`
         */
        public static class ViewErrorMessageOnVerificationPage implements Event {




            @Override
            public String getName() {
                return "view_Error_message_on_Verification_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Error");
                    properties.put("item_type", "message");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Verification");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user view the completion message after successfully completed onboarding. Event name: `view_Onboarding_Completed_page`
         */
        public static class ViewOnboardingCompletedPage implements Event {




            @Override
            public String getName() {
                return "view_Onboarding_Completed_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Onboarding_Completed");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the new code link to receive a new SMS with verification code. Event name: `click_New_Code_link_on_Verification_page`
         */
        public static class ClickNewCodeLinkOnVerificationPage implements Event {

            private Integer verificationCodeCount;



            public ClickNewCodeLinkOnVerificationPage( Integer verificationCodeCount) {

                this.verificationCodeCount = verificationCodeCount;
            }

            @Override
            public String getName() {
                return "click_New_Code_link_on_Verification_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("verification_code_count", verificationCodeCount);

                    properties.put("item_name", "New_Code");
                    properties.put("item_type", "link");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Verification");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         existing user receives a popup message explaining the phone auth required. Event name: `view_Phone_Auth_popup`
         */
        public static class ViewPhoneAuthPopup implements Event {




            @Override
            public String getName() {
                return "view_Phone_Auth_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Phone_Auth");
                    properties.put("item_type", "popup");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         existing user clicks the button on the popup message to start phone auth flow. Event name: `click_Verify_button_on_Phone_Auth_popup`
         */
        public static class ClickVerifyButtonOnPhoneAuthPopup implements Event {




            @Override
            public String getName() {
                return "click_Verify_button_on_Phone_Auth_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Verify");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Phone_Auth");
                    properties.put("parent_type", "popup");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the Send Kin page where he sets up the Kin amount he wants to send to a friend. Event name: `view_Send_Kin_page`
         */
        public static class ViewSendKinPage implements Event {




            @Override
            public String getName() {
                return "view_Send_Kin_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Send_Kin");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks on the Send button to send Kin to a friend. Event name: `click_Send_button_on_Send_Kin_page`
         */
        public static class ClickSendButtonOnSendKinPage implements Event {

            private Integer kinAmount;



            public ClickSendButtonOnSendKinPage( Integer kinAmount) {

                this.kinAmount = kinAmount;
            }

            @Override
            public String getName() {
                return "click_Send_button_on_Send_Kin_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("KIN_amount", kinAmount);

                    properties.put("item_name", "Send");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Send_Kin");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the success message on successful transaction of Kin to a friend. Event name: `view_Success_message_on_Send_Kin_page`
         */
        public static class ViewSuccessMessageOnSendKinPage implements Event {

            private Integer kinAmount;



            public ViewSuccessMessageOnSendKinPage( Integer kinAmount) {

                this.kinAmount = kinAmount;
            }

            @Override
            public String getName() {
                return "view_Success_message_on_Send_Kin_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("KIN_amount", kinAmount);

                    properties.put("item_name", "Success");
                    properties.put("item_type", "message");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Send_Kin");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views error message popup on several use cases on Send Kin page. Event name: `view_Error_popup_on_Send_Kin_page`
         */
        public static class ViewErrorPopupOnSendKinPage implements Event {

            private String errorType;



            public ViewErrorPopupOnSendKinPage( String errorType) {

                this.errorType = errorType;
            }

            @Override
            public String getName() {
                return "view_Error_popup_on_Send_Kin_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("error_type", errorType);

                    properties.put("item_name", "Error");
                    properties.put("item_type", "popup");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Send_Kin");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the Video page as part of a "tip" task . Event name: `view_Video_page`
         */
        public static class ViewVideoPage implements Event {




            @Override
            public String getName() {
                return "view_Video_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Video");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user playes the Video page as part of a "tip" task . Event name: `click_Play_button_on_Video_page`
         */
        public static class ClickPlayButtonOnVideoPage implements Event {

            private String videoTitle;



            public ClickPlayButtonOnVideoPage( String videoTitle) {

                this.videoTitle = videoTitle;
            }

            @Override
            public String getName() {
                return "click_Play_button_on_Video_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("video_title", videoTitle);

                    properties.put("item_name", "Play");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Video");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the backup intro page. Event name: `view_Backup_Intro_page`
         */
        public static class ViewBackupIntroPage implements Event {




            @Override
            public String getName() {
                return "view_Backup_Intro_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Backup_Intro");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the button on the backup intro page to start the backup flow. Event name: `click_Backup_button_on_Backup_Intro_page`
         */
        public static class ClickBackupButtonOnBackupIntroPage implements Event {




            @Override
            public String getName() {
                return "click_Backup_button_on_Backup_Intro_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Backup");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Backup_Intro");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views any of the steps on the backup flow (total of 5 steps). Event name: `view_Backup_Flow_page`
         */
        public static class ViewBackupFlowPage implements Event {

            private String backupFlowStep;



            public ViewBackupFlowPage( String backupFlowStep) {

                this.backupFlowStep = backupFlowStep;
            }

            @Override
            public String getName() {
                return "view_Backup_Flow_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("backup_flow_step", backupFlowStep);

                    properties.put("item_name", "Backup_Flow");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the complete button on each step to move to next step / finish the flow. Event name: `click_Completed_Step_button_on_Backup_Flow_page`
         */
        public static class ClickCompletedStepButtonOnBackupFlowPage implements Event {

            private String backupFlowStep;



            public ClickCompletedStepButtonOnBackupFlowPage( String backupFlowStep) {

                this.backupFlowStep = backupFlowStep;
            }

            @Override
            public String getName() {
                return "click_Completed_Step_button_on_Backup_Flow_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("backup_flow_step", backupFlowStep);

                    properties.put("item_name", "Completed_Step");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Backup_Flow");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the completion message after successfully completed backup flow. Event name: `view_Backup_Completed_page`
         */
        public static class ViewBackupCompletedPage implements Event {




            @Override
            public String getName() {
                return "view_Backup_Completed_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Backup_Completed");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the backup notification popup after completing last earn activity for day 1/7/14/30. Event name: `view_Backup_Notification_popup`
         */
        public static class ViewBackupNotificationPopup implements Event {

            private String backupNotificationType;



            public ViewBackupNotificationPopup( String backupNotificationType) {

                this.backupNotificationType = backupNotificationType;
            }

            @Override
            public String getName() {
                return "view_Backup_Notification_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("backup_notification_type", backupNotificationType);

                    properties.put("item_name", "Backup_Notification");
                    properties.put("item_type", "popup");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the backup button to start the backup flow (navigates to backup intro page). Event name: `click_Backup_button_on_Backup_Notification_popup`
         */
        public static class ClickBackupButtonOnBackupNotificationPopup implements Event {

            private String backupNotificationType;



            public ClickBackupButtonOnBackupNotificationPopup( String backupNotificationType) {

                this.backupNotificationType = backupNotificationType;
            }

            @Override
            public String getName() {
                return "click_Backup_button_on_Backup_Notification_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("backup_notification_type", backupNotificationType);

                    properties.put("item_name", "Backup");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Backup_Notification");
                    properties.put("parent_type", "popup");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         existing user views welcome back page after completing phone verification. Event name: `view_Welcome_Back_page`
         */
        public static class ViewWelcomeBackPage implements Event {




            @Override
            public String getName() {
                return "view_Welcome_Back_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Welcome_Back");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user chooses to restore wallet on welcome back page. Event name: `click_Restore_Wallet_button_on_Welcome_Back_page`
         */
        public static class ClickRestoreWalletButtonOnWelcomeBackPage implements Event {




            @Override
            public String getName() {
                return "click_Restore_Wallet_button_on_Welcome_Back_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Restore_Wallet");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Welcome_Back");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user chooses to create new wallet on welcome back page. Event name: `click_Create_New_Wallet_button_on_Welcome_Back_page`
         */
        public static class ClickCreateNewWalletButtonOnWelcomeBackPage implements Event {




            @Override
            public String getName() {
                return "click_Create_New_Wallet_button_on_Welcome_Back_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Create_New_Wallet");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Welcome_Back");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the scan page after staring the restore flow. Event name: `view_Scan_page`
         */
        public static class ViewScanPage implements Event {




            @Override
            public String getName() {
                return "view_Scan_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Scan");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the Scan button to start scanning the process of the QR code. Event name: `click_Scan_button_on_2_Steps_Away_page`
         */
        public static class ClickScanButtonOn2StepsAwayPage implements Event {




            @Override
            public String getName() {
                return "click_Scan_button_on_2_Steps_Away_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Scan");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "2_Steps_Away");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the security questions page as part of the restore flow. Event name: `view_Answer_Security_Questions_page`
         */
        public static class ViewAnswerSecurityQuestionsPage implements Event {




            @Override
            public String getName() {
                return "view_Answer_Security_Questions_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Answer_Security_Questions");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user confirms the answers entered for security questions as part of the restore flow (the button is "Next" and not confirm). Event name: `click_Confirm_button_on_Answer_Security_Questions_page`
         */
        public static class ClickConfirmButtonOnAnswerSecurityQuestionsPage implements Event {




            @Override
            public String getName() {
                return "click_Confirm_button_on_Answer_Security_Questions_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Confirm");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Answer_Security_Questions");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the completion message after successfully restoring the wallet. Event name: `view_Wallet_Restored_page`
         */
        public static class ViewWalletRestoredPage implements Event {




            @Override
            public String getName() {
                return "view_Wallet_Restored_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Wallet_Restored");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the creating wallet page (animation) when creating new wallet. Event name: `view_Creating_Wallet_page`
         */
        public static class ViewCreatingWalletPage implements Event {




            @Override
            public String getName() {
                return "view_Creating_Wallet_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Creating_Wallet");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks on backup button on More page. Event name: `click_Backup_button_on_More_page`
         */
        public static class ClickBackupButtonOnMorePage implements Event {

            private Boolean alreadyBackedUp;



            public ClickBackupButtonOnMorePage( Boolean alreadyBackedUp) {

                this.alreadyBackedUp = alreadyBackedUp;
            }

            @Override
            public String getName() {
                return "click_Backup_button_on_More_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("already_backed_up", alreadyBackedUp);

                    properties.put("item_name", "Backup");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "More");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views the FAQ main page (with all the categories). Event name: `view_FAQ_Main_page`
         */
        public static class ViewFaqMainPage implements Event {




            @Override
            public String getName() {
                return "view_FAQ_Main_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "FAQ_Main");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user views a FAQ specific page . Event name: `view_FAQ_page`
         */
        public static class ViewFaqPage implements Event {

            private String faqCategory;

            private String faqSubcategory;



            public ViewFaqPage( String faqCategory, String faqSubcategory) {

                this.faqCategory = faqCategory;
                this.faqSubcategory = faqSubcategory;
            }

            @Override
            public String getName() {
                return "view_FAQ_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("FAQ_category", faqCategory);
                    properties.put("FAQ_subcategory", faqSubcategory);

                    properties.put("item_name", "FAQ");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the feedback button on the More page. Event name: `click_Feedback_button`
         */
        public static class ClickFeedbackButton implements Event {




            @Override
            public String getName() {
                return "click_Feedback_button";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Feedback");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the Yes/No buttons on FAQ page, to share if the page info was helpful or not. Event name: `click_Page_Helpful_button_on_FAQ_page`
         */
        public static class ClickPageHelpfulButtonOnFaqPage implements Event {

            private String faqCategory;

            private String faqSubcategory;

            private Boolean helpful;



            public ClickPageHelpfulButtonOnFaqPage( String faqCategory, String faqSubcategory, Boolean helpful) {

                this.faqCategory = faqCategory;
                this.faqSubcategory = faqSubcategory;
                this.helpful = helpful;
            }

            @Override
            public String getName() {
                return "click_Page_Helpful_button_on_FAQ_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("FAQ_category", faqCategory);
                    properties.put("FAQ_subcategory", faqSubcategory);
                    properties.put("Helpful", helpful);

                    properties.put("item_name", "Page_Helpful");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "FAQ");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User views captcha popup. Event name: `view_Captcha_popup`
         */
        public static class ViewCaptchaPopup implements Event {




            @Override
            public String getName() {
                return "view_Captcha_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Captcha");
                    properties.put("item_type", "popup");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User views a campaign popup after completing a task with a campaign related. Event name: `view_Campaign_popup`
         */
        public static class ViewCampaignPopup implements Event {

            private String campaignName;

            private String taskId;



            public ViewCampaignPopup( String campaignName, String taskId) {

                this.campaignName = campaignName;
                this.taskId = taskId;
            }

            @Override
            public String getName() {
                return "view_Campaign_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("campaign_name", campaignName);
                    properties.put("task_id", taskId);

                    properties.put("item_name", "Campaign");
                    properties.put("item_type", "popup");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks on a button that open a campaign link, opens a web browser . Event name: `click_Link_button_on_Campaign_popup`
         */
        public static class ClickLinkButtonOnCampaignPopup implements Event {

            private String campaignName;

            private String taskId;



            public ClickLinkButtonOnCampaignPopup( String campaignName, String taskId) {

                this.campaignName = campaignName;
                this.taskId = taskId;
            }

            @Override
            public String getName() {
                return "click_Link_button_on_Campaign_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("campaign_name", campaignName);
                    properties.put("task_id", taskId);

                    properties.put("item_name", "Link");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Campaign");
                    properties.put("parent_type", "popup");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User views the task category page with all tasks options. Event name: `view_Task_Categories_page`
         */
        public static class ViewTaskCategoriesPage implements Event {




            @Override
            public String getName() {
                return "view_Task_Categories_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Task_Categories");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks a specific task category. Event name: `click_Category_button_on_Task_Categories_page`
         */
        public static class ClickCategoryButtonOnTaskCategoriesPage implements Event {

            private String taskCategory;



            public ClickCategoryButtonOnTaskCategoriesPage( String taskCategory) {

                this.taskCategory = taskCategory;
            }

            @Override
            public String getName() {
                return "click_Category_button_on_Task_Categories_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("task_category", taskCategory);

                    properties.put("item_name", "Category");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Task_Categories");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User views Learn More page about any topic (event can be triggered for any Learn page we will have). Event name: `view_Learn_More_page`
         */
        public static class ViewLearnMorePage implements Event {

            private String pageContent;



            public ViewLearnMorePage( String pageContent) {

                this.pageContent = pageContent;
            }

            @Override
            public String getName() {
                return "view_Learn_More_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("page_content", pageContent);

                    properties.put("item_name", "Learn_More");
                    properties.put("item_type", "page");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks the "I need help" button to get help. Event name: `click_Help_button_on_More_page`
         */
        public static class ClickHelpButtonOnMorePage implements Event {




            @Override
            public String getName() {
                return "click_Help_button_on_More_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Help");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "More");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks the "contact us" button after choosing a category and a sub category from the FAQ page. Event name: `click_Contact_button_on_Specific_FAQ_page`
         */
        public static class ClickContactButtonOnSpecificFaqPage implements Event {

            private String faqCategory;

            private String faqSubcategory;



            public ClickContactButtonOnSpecificFaqPage( String faqCategory, String faqSubcategory) {

                this.faqCategory = faqCategory;
                this.faqSubcategory = faqSubcategory;
            }

            @Override
            public String getName() {
                return "click_Contact_button_on_Specific_FAQ_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("FAQ_category", faqCategory);
                    properties.put("FAQ_subcategory", faqSubcategory);

                    properties.put("item_name", "Contact");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Specific_FAQ");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks on the "submit" button to send a Support form. Event name: `click_Submit_button_on_Support_form_page`
         */
        public static class ClickSubmitButtonOnSupportFormPage implements Event {

            private String faqCategory;

            private String faqSubcategory;



            public ClickSubmitButtonOnSupportFormPage( String faqCategory, String faqSubcategory) {

                this.faqCategory = faqCategory;
                this.faqSubcategory = faqSubcategory;
            }

            @Override
            public String getName() {
                return "click_Submit_button_on_Support_form_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("FAQ_category", faqCategory);
                    properties.put("FAQ_subcategory", faqSubcategory);

                    properties.put("item_name", "Submit");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Support_form");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks on the "submit" button to send a Feedback form. Event name: `click_Submit_button_on_Feedback_form`
         */
        public static class ClickSubmitButtonOnFeedbackForm implements Event {




            @Override
            public String getName() {
                return "click_Submit_button_on_Feedback_form";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Submit");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Feedback");
                    properties.put("parent_type", "form");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User click on "Yes" or "No" to tell us whether the FAQ page was helpful or not. Event name: `click_Helpful_button_on_FAQ_page`
         */
        public static class ClickHelpfulButtonOnFaqPage implements Event {

            private Boolean helpful;



            public ClickHelpfulButtonOnFaqPage( Boolean helpful) {

                this.helpful = helpful;
            }

            @Override
            public String getName() {
                return "click_Helpful_button_on_FAQ_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("Helpful", helpful);

                    properties.put("item_name", "Helpful");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "FAQ");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks on "Start Now" to choose fav content. Event name: `click_Start_Now_button_on_Fav_Topics_popup`
         */
        public static class ClickStartNowButtonOnFavTopicsPopup implements Event {




            @Override
            public String getName() {
                return "click_Start_Now_button_on_Fav_Topics_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Start_Now");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Fav_Topics");
                    properties.put("parent_type", "popup");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks on "My Topics" to choose fav content. Event name: `click_My_Topics_button_on_More_page`
         */
        public static class ClickMyTopicsButtonOnMorePage implements Event {




            @Override
            public String getName() {
                return "click_My_Topics_button_on_More_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "My_Topics");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "More");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks "Done" after choosing at least 5 topics he/she's interested in. Event name: `click_Done_button_on_Topics_page`
         */
        public static class ClickDoneButtonOnTopicsPage implements Event {

            private String topic;



            public ClickDoneButtonOnTopicsPage( String topic) {

                this.topic = topic;
            }

            @Override
            public String getName() {
                return "click_Done_button_on_Topics_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("topic", topic);

                    properties.put("item_name", "Done");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Topics");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks on Backup Now in the Backup my Kin page . Event name: `click_Backup_Now_button_on_Backup_My_Kin_page`
         */
        public static class ClickBackupNowButtonOnBackupMyKinPage implements Event {




            @Override
            public String getName() {
                return "click_Backup_Now_button_on_Backup_My_Kin_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Backup_Now");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Backup_My_Kin");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks Confirm after entering a 4-digit code to approve a QR code was received to his/her mail. Event name: `click_Confirm_button_on_Check_Mail_page`
         */
        public static class ClickConfirmButtonOnCheckMailPage implements Event {




            @Override
            public String getName() {
                return "click_Confirm_button_on_Check_Mail_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Confirm");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Check_Mail");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks the Upload button to upload his/her saved QR code. Event name: `click_Upload_QR_button_on_2_Steps_Away_page`
         */
        public static class ClickUploadQrButtonOn2StepsAwayPage implements Event {




            @Override
            public String getName() {
                return "click_Upload_QR_button_on_2_Steps_Away_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Upload_QR");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "2_Steps_Away");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User clicks Next after filling the 2 security answers. Event name: `click_Next_button_on_1_Step_Away_page`
         */
        public static class ClickNextButtonOn1StepAwayPage implements Event {




            @Override
            public String getName() {
                return "click_Next_button_on_1_Step_Away_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Next");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "1_Step_Away");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User chooses to re-backup his/her wallet on a popup that will display to 50% of users who already backed up in the past. Event name: `click_Update_Backup_button_on_Getting_Better_popup`
         */
        public static class ClickUpdateBackupButtonOnGettingBetterPopup implements Event {




            @Override
            public String getName() {
                return "click_Update_Backup_button_on_Getting_Better_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Update_Backup");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Getting_Better");
                    properties.put("parent_type", "popup");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User views the "We're getting better" popup that will display to 50% of users who already backed up in the past. Event name: `view_Getting_Better_popup`
         */
        public static class ViewGettingBetterPopup implements Event {




            @Override
            public String getName() {
                return "view_Getting_Better_popup";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("item_name", "Getting_Better");
                    properties.put("item_type", "popup");
                    properties.put("action", "view");
                    properties.put("event_type", "analytics");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         user clicks to purchase a spending offer. Event name: `click_Buy_button_on_Offer_page`
         */
        public static class ClickBuyButtonOnOfferPage implements Event {

            private String brandName;

            private Integer kinPrice;

            private String offerCategory;

            private String offerId;

            private String offerName;

            private String offerType;



            public ClickBuyButtonOnOfferPage( String brandName, Integer kinPrice, String offerCategory, String offerId, String offerName, String offerType) {

                this.brandName = brandName;
                this.kinPrice = kinPrice;
                this.offerCategory = offerCategory;
                this.offerId = offerId;
                this.offerName = offerName;
                this.offerType = offerType;
            }

            @Override
            public String getName() {
                return "click_Buy_button_on_Offer_page";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("brand_name", brandName);
                    properties.put("KIN_price", kinPrice);
                    properties.put("offer_category", offerCategory);
                    properties.put("offer_id", offerId);
                    properties.put("offer_name", offerName);
                    properties.put("offer_type", offerType);

                    properties.put("item_name", "Buy");
                    properties.put("item_type", "button");
                    properties.put("action", "click");
                    properties.put("event_type", "analytics");
                    properties.put("parent_name", "Offer");
                    properties.put("parent_type", "page");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

    }

    public static class BILog {

        /**
         An error occurred while updating the user's balance using the client blockchain sdk (on app launch, after task completion, after purchase). Event name: `balance_update_failed`
         */
        public static class BalanceUpdateFailed implements Event {

            private String failureReason;



            public BalanceUpdateFailed( String failureReason) {

                this.failureReason = failureReason;
            }

            @Override
            public String getName() {
                return "balance_update_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);

                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         An error occured while creating the stellar account or when funding it with lumens. Event name: `stellar_account_creation_failed`
         */
        public static class StellarAccountCreationFailed implements Event {

            private String failureReason;



            public StellarAccountCreationFailed( String failureReason) {

                this.failureReason = failureReason;
            }

            @Override
            public String getName() {
                return "stellar_account_creation_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);

                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         Our server created the stellar account successfully and funded it with lumens. Event name: `stellar_account_creation_succeeded`
         */
        public static class StellarAccountCreationSucceeded implements Event {




            @Override
            public String getName() {
                return "stellar_account_creation_succeeded";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         An error occurred while activating the client account using client blockchain sdk. Event name: `stellar_kin_trustline_setup_failed`
         */
        public static class StellarKinTrustlineSetupFailed implements Event {

            private String failureReason;



            public StellarKinTrustlineSetupFailed( String failureReason) {

                this.failureReason = failureReason;
            }

            @Override
            public String getName() {
                return "stellar_kin_trustline_setup_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);

                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         Client successfully activated the account using client blockchain sdk. Event name: `stellar_kin_trustline_setup_succeeded`
         */
        public static class StellarKinTrustlineSetupSucceeded implements Event {




            @Override
            public String getName() {
                return "stellar_kin_trustline_setup_succeeded";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         User registration failed. Event name: `user_registration_failed`
         */
        public static class UserRegistrationFailed implements Event {

            private String failureReason;



            public UserRegistrationFailed( String failureReason) {

                this.failureReason = failureReason;
            }

            @Override
            public String getName() {
                return "user_registration_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);

                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         When formatting of the phone inserted by the user when validating fails. Event name: `phone_formatting_failed`
         */
        public static class PhoneFormattingFailed implements Event {




            @Override
            public String getName() {
                return "phone_formatting_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         When server sends the auth token via push, and the client receives it.. Event name: `auth_token_received`
         */
        public static class AuthTokenReceived implements Event {




            @Override
            public String getName() {
                return "auth_token_received";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         When asking the auth token to the server fails.. Event name: `auth_token_ack_failed`
         */
        public static class AuthTokenAckFailed implements Event {

            private String failureReason;



            public AuthTokenAckFailed( String failureReason) {

                this.failureReason = failureReason;
            }

            @Override
            public String getName() {
                return "auth_token_ack_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);

                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

        /**
         When captcha fails or is cancelled . Event name: `captcha_failed`
         */
        public static class CaptchaFailed implements Event {

            private String failureReason;



            public CaptchaFailed( String failureReason) {

                this.failureReason = failureReason;
            }

            @Override
            public String getName() {
                return "captcha_failed";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {

                    properties.put("failure_reason", failureReason);

                    properties.put("event_type", "log");
                } catch (JSONException e) {
                    Log.e("Events",
                            "Exception " + e + ", while building JSONObject properties of " + this.getClass().getName());
                }
                return properties;
            }
        }

    }

}