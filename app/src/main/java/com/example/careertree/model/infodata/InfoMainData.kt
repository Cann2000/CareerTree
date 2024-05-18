package com.example.careertree.model.infodata

import com.example.careertree.model.ai.Ai
import com.example.careertree.model.mobile.Mobile
import com.example.careertree.model.blog.Blog
import com.example.careertree.model.cyber_security.CyberSecurity
import com.example.careertree.model.data_science.DataScience
import com.example.careertree.model.event.Event
import com.example.careertree.model.game.Game
import com.example.careertree.model.homepage.HomePageContent
import com.example.careertree.model.web.Web

data class InfoMainData(

    val version_name:String,

    val contents: List<HomePageContent>,

    val blogs: List<Blog>,

    val events: List<Event>,

    val mobile: Mobile,

    val web: Web,

    val game: Game,

    val ai: Ai,

    val cyber_security: CyberSecurity,

    val data_science: DataScience

    ) {

}