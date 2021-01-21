package hui.ait.finalproject.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import hui.ait.finalproject.ActionsActivity
import hui.ait.finalproject.R
import hui.ait.finalproject.data.Post
import kotlinx.android.synthetic.main.new_post_dialog.view.*
import java.lang.RuntimeException

class PostDialog : DialogFragment() {

    interface PostHandler {
        fun postCreated(post: Post)
        fun postUpdated(post: Post)
    }

    private lateinit var postHandler: PostHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is PostHandler) {
            postHandler = context
        } else {
            throw RuntimeException(getString(R.string.handler_error))
        }
    }

    private lateinit var etPostTitle: EditText
    private lateinit var etPostLocation: EditText
    private lateinit var etPostBody: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_post))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_post_dialog, null
        )

        etPostTitle = rootView.etPostTitle
        etPostLocation = rootView.etPostLocation
        etPostBody = rootView.etPostBody
        builder.setView(rootView)

        val arguments = this.arguments

        if (arguments != null && arguments.containsKey(
                ActionsActivity.KEY_ITEM_TO_EDIT)) {

            val post = arguments.getSerializable(
                ActionsActivity.KEY_ITEM_TO_EDIT
            ) as Post

            etPostTitle.setText(post.postTitle)
            etPostLocation.setText(post.postLocation)
            etPostBody.setText(post.postBody)

            builder.setTitle(getString(R.string.edit_post))
        }

        builder.setPositiveButton(getString(R.string.ok)) {
                dialog, witch -> // empty
        }

        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etPostTitle.text.isNotEmpty()) {
                val arguments = this.arguments

                if (arguments != null && arguments.containsKey(ActionsActivity.KEY_ITEM_TO_EDIT)) {
                    handlePostEdit()
                } else {
                    handlePostCreate()
                }

                dialog.dismiss()
            } else {
                etPostTitle.error = getString(R.string.post_error)
            }
        }
    }

    private fun handlePostCreate() {
        postHandler.postCreated(
            Post(
                null,
                etPostTitle.text.toString(),
                etPostLocation.text.toString(),
                etPostBody.text.toString()
            )
        )
    }

    private fun handlePostEdit() {
        val postToEdit = arguments?.getSerializable(
            ActionsActivity.KEY_ITEM_TO_EDIT
        ) as Post
        postToEdit.postTitle = etPostTitle.text.toString()
        postToEdit.postLocation = etPostLocation.text.toString()
        postToEdit.postBody = etPostBody.text.toString()

        postHandler.postUpdated(postToEdit)
    }

}