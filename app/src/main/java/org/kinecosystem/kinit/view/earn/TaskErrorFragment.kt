package org.kinecosystem.kinit.view.earn


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickCloseButtonOnErrorPage
import org.kinecosystem.kinit.analytics.Events.Analytics.ViewErrorPage
import org.kinecosystem.kinit.analytics.Events.Event
import org.kinecosystem.kinit.model.earn.hasPostActions
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.BaseActivity
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.customView.AlertManager
import javax.inject.Inject


class TaskErrorFragment : BaseFragment() {
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var taskRepository: TasksRepository
    private var seenDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var errorType = ERROR_TRANSACTION
        if (arguments != null) {
            errorType = arguments!!.getInt(ARG_ERROR_TYPE, ERROR_TRANSACTION)
        }
        reportViewEvent(errorType)
        val view = inflater.inflate(getLayout(errorType), container, false)
        view.findViewById<View>(R.id.close).setOnClickListener { view1 ->
            reportClickOnCloseEvent(errorType)
            val activity = activity as BaseActivity?
            taskRepository?.resetTaskState()
            if (shouldShowActionDialog(errorType)) {
                showActionDialog()
            } else {
                activity?.finish()
            }
        }
        return view
    }

    private fun getLayout(errorType: Int): Int {
        if (arguments != null && arguments!!.containsKey(ARG_ERROR_TYPE)) {
            if (errorType == ERROR_SUBMIT) {
                return R.layout.earn_error_submit_layout
            }
            if (errorType == ERROR_TRANSACTION) {
                return R.layout.earn_error_transaction_layout
            }
        }
        return R.layout.earn_error_submit_layout
    }

    private fun reportViewEvent(errorType: Int) {
        val event: Event
        event = if (errorType == ERROR_TRANSACTION) {
            ViewErrorPage(Analytics.VIEW_ERROR_TYPE_REWARD)
        } else { // ERROR_SUBMIT
            ViewErrorPage(Analytics.VIEW_ERROR_TYPE_TASK_SUBMISSION)
        }
        analytics?.logEvent(event)
    }

    private fun reportClickOnCloseEvent(errorType: Int) {
        val event: Event
        if (errorType == ERROR_TRANSACTION) {
            event = ClickCloseButtonOnErrorPage(Analytics.VIEW_ERROR_TYPE_REWARD)
        } else { // ERROR_SUBMIT
            event = ClickCloseButtonOnErrorPage(Analytics.VIEW_ERROR_TYPE_TASK_SUBMISSION)
        }
        analytics?.logEvent(event)
    }

    private fun shouldShowActionDialog(errorType: Int): Boolean {
        val hasActions = taskRepository.taskInProgress?.hasPostActions()
        return errorType == ERROR_TRANSACTION && !seenDialog && hasActions == true
    }

    private fun showActionDialog() {
        taskRepository.taskInProgress?.let {
            val taskId = it.id
            with(it.postTaskActions.orEmpty().first()) {
                if (context != null) {
                    AlertManager.showGeneralAlert(context!!, title, text, positiveText, {
                        if (context != null) {
                            GeneralUtils.navigateToUrl(context, url)
                            analytics.logEvent(Events.Analytics.ClickLinkButtonOnCampaignPopup(actionName, taskId))
                        }
                    }, negativeText, {}, iconUrl)
                    analytics.logEvent(Events.Analytics.ViewCampaignPopup(actionName, taskId))
                    seenDialog = true
                }

            }
        }
    }

    companion object {

        const val ARG_TASK_ID = "TaskErrorFragment_ARG_TASK_ID"
        const val INVALID_TASK_ID = ""

        val ARG_ERROR_TYPE = "Earn_error_type"
        val ERROR_SUBMIT = 10
        val ERROR_TRANSACTION = 20

        val TAG = TaskErrorFragment::class.java.simpleName

        fun newInstance(taskId:String, error: Int): TaskErrorFragment {
            val fragment = TaskErrorFragment()
            val args = Bundle()
            args.putInt(ARG_ERROR_TYPE, error)
            args.putString(ARG_TASK_ID, taskId)
            fragment.arguments = args
            return fragment
        }
    }
}
