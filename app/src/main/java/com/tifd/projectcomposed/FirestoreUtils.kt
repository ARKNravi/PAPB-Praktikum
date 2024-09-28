package com.tifd.projectcomposed.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.tifd.projectcomposed.ScheduleItem

fun addScheduleDataToFirestore() {
    val db = FirebaseFirestore.getInstance()
    val scheduleCollection = db.collection("schedules")

    val scheduleItems = listOf(
        ScheduleItem("Senin", "08:40", "14:29", "C", "CIF65318", "Jaringan Saraf Tiruan", "Dr. Eng. Budi Darma Setiawan, S.Kom., M.Cs.", "Lab Pembelajaran Gedung G - G1.4"),
        ScheduleItem("Senin", "12:50", "14:29", "B", "CIF60048","Machine Learning Operations (MLOps)", "Rizal Setya Perdana, S.Kom., M.Kom., Ph.D.", "Gedung F FILKOM - F3.10"),
        ScheduleItem("Senin", "14:30", "16:19", "C", "CIF65318","Jaringan Saraf Tiruan ", "Dr. Eng. Budi Darma Setiawan, S.Kom., M.Cs.", "Gedung F FILKOM - F3.6"),
        ScheduleItem("Senin", "16:20", "18:09", "D", "CIF65217","Jaringan Nirkabel", "Widhi Yahya, S.Kom., M.Sc., M.T., Ph.D.", "Gedung F FILKOM - F2.2"),
        ScheduleItem("Selasa", "07:00", "08:39", "D", "CIF65115","Pengembangan Aplikasi Perangkat Bergerak", "Agi Putra Kharisma, ST., MT.", "Lab Pembelajaran Gedung G - G1.3"),
        ScheduleItem("Rabu", "07:00", "09:29", "C", "COM60051","Metodologi Penelitian dan Penulisan Ilmiah  ", "Dr.Eng. Irawati Nurmala Sari, S.Kom., M.Sc.", "Gedung F FILKOM - F3.10"),
        ScheduleItem("Rabu", "12:50", "14:29", "D", "CIF65115","Pengembangan Aplikasi Perangkat Bergerak", "Agi Putra Kharisma, ST., MT.", "Gedung F FILKOM - F2.8"),
        ScheduleItem("Rabu", "15:30", "16:19", "D", "CIF65217","Jaringan Nirkabel ", "Widhi Yahya, S.Kom., M.Sc., M.T., Ph.D.", "Gedung F FILKOM - F2.6"),
        ScheduleItem("Kamis", "12:50", "14:29", "B", "CIF60048","Machine Learning Operations (MLOps)", "Rizal Setya Perdana, S.Kom., M.Kom., Ph.D.", "Gedung F FILKOM - F3.9"),
        ScheduleItem("Kamis", "14:30", "16:19", "Q", "CIF61101","Rekayasa Perangkat Lunak", "Fais Al Huda, S.Kom., M.Kom.", "Gedung F FILKOM - F4.9"),
        ScheduleItem("Jumat", "12:50", "14:29", "F", "CIF65319","Statistika inferensi", "Dr.Eng. Irawati Nurmala Sari, S.Kom., M.Sc.", "Gedung F FILKOM - F3.9"),
        ScheduleItem("Jumat", "14:30", "16:19", "Q", "CIF61101","Rekayasa Perangkat Lunak", "Fais Al Huda, S.Kom., M.Kom.", "Gedung F FILKOM - F2.5")
    )

    scheduleCollection.get().addOnSuccessListener { result ->
        val existingItems = result.map { it.toObject(ScheduleItem::class.java) }.toSet()
        scheduleItems.forEach { newItem ->
            if (newItem !in existingItems) {
                scheduleCollection.add(newItem)
            }
        }
    }
}
