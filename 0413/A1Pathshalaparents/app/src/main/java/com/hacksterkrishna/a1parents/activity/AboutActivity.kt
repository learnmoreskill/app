package com.hacksterkrishna.a1parents.activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.danielstone.materialaboutlibrary.ConvenienceBuilder
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.hacksterkrishna.a1parents.R

/**
 * Created by krishna on 31/12/17.
 */
class AboutActivity : MaterialAboutActivity() {

    var colorIcon = R.color.mal_color_icon_light_theme

    override fun getActivityTitle(): CharSequence? {
        return "About"
    }

    override fun getMaterialAboutList(c: Context): MaterialAboutList {

        val appCardBuilder = MaterialAboutCard.Builder()

        appCardBuilder.addItem(MaterialAboutTitleItem.Builder()
                .text("A1Pathshala Parents")
                .desc("© 2017 Grab Infotech")
                .icon(R.mipmap.launcher)
                .build())

        appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_info_outline)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Version",
                false))

        /*appCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text("Open source libraries")
                .icon(IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_github_circle)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnClickAction {
                    LibsBuilder()
                            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                            .withAutoDetect(true)
                            .withFields(R.string::class.java.fields)
                            .withAboutIconShown(true)
                            .withAboutVersionShown(true)
                            .start(this)
                }
                .build())*/

        val detailsCardBuilder = MaterialAboutCard.Builder()

        detailsCardBuilder.title("Details")

        detailsCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_information_outline)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Version",
                false))

        detailsCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
                IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_earth)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Visit Website",
                true,
                Uri.parse("https://a1pathshala.com")))

        detailsCardBuilder.addItem(ConvenienceBuilder.createEmailItem(c,
                IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_email)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Send an email",
                true,
                "contact@hacksterkrishna.com",
                "Regarding A1Pathshala"))

        detailsCardBuilder.addItem(ConvenienceBuilder.createPhoneItem(c,
                IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_phone)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Contact",
                true,
                "+9779814299165"))

        detailsCardBuilder.addItem(ConvenienceBuilder.createMapItem(c,
                IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_map)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Office",
                null,
                "Grab Technology,Shantinagar,Kathmandu"))

        return MaterialAboutList(appCardBuilder.build(), detailsCardBuilder.build())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                //NavUtils.navigateUpFromSameTask(this)
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}