package rishi.alphae.sampleapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by rishi on 3/2/19.
 */
abstract class UserAdapter(users:ArrayList<User> ): RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    val mUsers = users

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUsers[position]
        holder.userName.text = user.name
        holder.userAge.text = user.age.toString()

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var userName: TextView = view.findViewById<TextView>(R.id.user_name)
        var userAge: TextView = view.findViewById<TextView>(R.id.user_age)

    }

}