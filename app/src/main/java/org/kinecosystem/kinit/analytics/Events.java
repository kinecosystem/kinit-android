package org.kinecosystem.kinit.analytics;//
// Events.java
//
// Don't edit this file.
// Generated at 2018-05-28 11:05:49 +0000 by Kik BI-Generator.
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

    public static class DialogErrorType {
        public static final String EXCEED_MIN_MAX = "Exceed max/min Kin";
        public static final String FRIEND_HAS_NO_ADDRESS = "Friend not exists";
        public static final String NOT_ENOUGH_BALANCE = "Exceed existing Kin";
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

            private Float kinAmount;

            private String transactionType;



            public KINTransactionFailed( String failureReason, Float kinAmount, String transactionType) {

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

            private Float kinAmount;

            private String transactionId;

            private String transactionType;



            public KINTransactionSucceeded( Float kinAmount, String transactionId, String transactionType) {

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




            @Override
            public String getName() {
                return "support_request_sent";
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
         users clicks on support button (opens email). Event name: `click_Support_button`
         */
        public static class ClickSupportButton implements Event {




            @Override
            public String getName() {
                return "click_Support_button";
            }

            @Override
            public JSONObject getProperties() {
                JSONObject properties = new JSONObject();
                try {


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



            public ViewErrorPage( String errorType) {

                this.errorType = errorType;
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



            public ViewEmptyStatePage( String menuItemName) {

                this.menuItemName = menuItemName;
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

            private Float kinAmount;



            public ClickSendButtonOnSendKinPage( Float kinAmount) {

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

            private Float kinAmount;



            public ViewSuccessMessageOnSendKinPage( Float kinAmount) {

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

    }

}