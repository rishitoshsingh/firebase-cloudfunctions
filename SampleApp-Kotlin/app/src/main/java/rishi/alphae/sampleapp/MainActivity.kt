package rishi.alphae.sampleapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private val mUsers: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance().getReference("User")
        val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val viewAdapter = object : UserAdapter(mUsers) {}

        user_recycler.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            itemAnimator = DefaultItemAnimator()
        }

        save_button.setOnClickListener {
            if (edit_user_name.text.toString() != "" && edit_user_age.text.toString() != "") {
                val userId: String = database.push().key!!
                database.child(userId).setValue(User(edit_user_name.text.toString(), edit_user_age.text.toString().toInt()))
                Snackbar.make(root_view, "Users Updated", Snackbar.LENGTH_SHORT).show()
            }
        }

        database.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val user = p0?.getValue(User::class.java)
                mUsers.add(user!!)
                viewAdapter.notifyDataSetChanged()
                progress_bar.visibility = View.GONE
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                val user = p0?.getValue(User::class.java)
                mUsers.remove(user)
                viewAdapter.notifyDataSetChanged()
            }
        })
    }
}