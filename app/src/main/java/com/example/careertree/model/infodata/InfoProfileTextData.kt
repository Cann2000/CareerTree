package com.example.careertree.model.infodata

import com.example.careertree.model.profile_text_data.Skill
import com.example.careertree.model.profile_text_data.University
import com.example.careertree.model.profile_text_data.UniversityDepartment

data class InfoProfileTextData(

    val universities: List<University>,

    val departments: List<UniversityDepartment>,

    val skills: List<Skill>

) {
}