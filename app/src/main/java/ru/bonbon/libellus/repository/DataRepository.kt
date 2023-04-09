package ru.bonbon.libellus.repository

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import ru.bonbon.libellus.model.ConsultDay
import ru.bonbon.libellus.model.Day

object DataRepository {
    private val database: FirebaseDatabase = Firebase.database
    private val scheduleReference: DatabaseReference = database.getReference("specialities")
    private val consultationsReference: DatabaseReference = database.getReference("consultations")
    lateinit var group: String
    lateinit var speciality: String

    fun fetchWeeks() = callbackFlow<List<String>>{
        val weeks = mutableListOf<String>()
        val eventListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.key != null){
                        weeks.add(it.key!!.replace("_", "."))
                    }
                }
                this@callbackFlow.trySendBlocking(weeks)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }

        }
        consultationsReference.addListenerForSingleValueEvent(eventListener)
        awaitClose {
            consultationsReference.removeEventListener(eventListener)
        }
    }

    fun fetchTeachers(week: String) = callbackFlow<List<String>>{
        val teachers = mutableListOf<String>()
        val eventListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child(week.replace(".", "_")).children.forEach {
                    if (it.key != null){
                        teachers.add(it.key!!.replace("_", " "))
                    }
                }
//                Log.d("GROP", teachers.toString())
                this@callbackFlow.trySendBlocking(teachers)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }

        }
        consultationsReference.addListenerForSingleValueEvent(eventListener)
        awaitClose {
            consultationsReference.removeEventListener(eventListener)
        }
    }


    fun fetchConsultations(week: String, teacher: String) = callbackFlow<List<ConsultDay>>{
        val postListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val node = snapshot
                    .child(week.replace(".", "_"))
                    .child(teacher.replace(" ", "_"))

                val targetDaysList = node
                    .getValue(object: GenericTypeIndicator<List<ConsultDay>>(){})!!
                this@callbackFlow.trySendBlocking(targetDaysList)
            }
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()

            }
        }
        consultationsReference.addValueEventListener(postListener)
        awaitClose {
            consultationsReference.removeEventListener(postListener)
        }
    }

    fun readScheduleOnce() = callbackFlow<Map<String, List<String>>>{
        val specialities: MutableMap<String, List<String>> = mutableMapOf()
        val eventListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (keyNode in snapshot.children){
                    val groups = ArrayList<String>()
                    keyNode.children.toList().forEach {
//                        Log.d("GROP", it.toString())
                        groups.add(it.key!!)
                    }
//                    Log.d("GROP", keyNode.key.toString())
                    specialities[keyNode.key.toString().replace("_", ".")] = groups
                }
//                Log.d("GROP", specialities.toString())
//                Log.d("GROP", specialities.keys.toString())
                this@callbackFlow.trySendBlocking(specialities)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }

        }
        scheduleReference.addListenerForSingleValueEvent(eventListener)
        awaitClose {
            scheduleReference.removeEventListener(eventListener)
        }
    }

    fun fetchSchedule() = callbackFlow<Pair<String, Map<String, Day>>>{
        val postListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupNode = snapshot
                    .child(speciality.replace(".", "_"))
                    .child(group)
                val targetGroup = Pair(
                    groupNode.key!!,
                    groupNode.getValue(object: GenericTypeIndicator<Map<String, Day>>(){})!!
                )
//                Log.d("FETCH_GROUPS", targetGroup.toString())
                this@callbackFlow.trySendBlocking(targetGroup)
            }
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()

            }
        }
        scheduleReference.addValueEventListener(postListener)
        awaitClose {
            scheduleReference.removeEventListener(postListener)
        }
    }
}