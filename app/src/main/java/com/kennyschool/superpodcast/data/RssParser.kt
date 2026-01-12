package com.kennyschool.superpodcast.data

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

/**
 * Super basic RSS parser.
 *
 * What I care about:
 * - <item> ... </item> = one episode
 * - <title>Episode name</title>
 * - <enclosure url="https://....mp3" />  (audio file)
 *
 * This is enough to meet the assignment requirement without overbuilding.
 */
object RssParser {

    fun parseEpisodes(xml: String): List<Episode> {
        val episodes = mutableListOf<Episode>()

        // XmlPullParser is built into Android and lightweight.
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = false

        val parser = factory.newPullParser()
        parser.setInput(xml.reader())

        var eventType = parser.eventType
        var insideItem = false

        var currentTitle: String? = null
        var currentAudioUrl: String? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    val tag = parser.name.lowercase()

                    if (tag == "item") {
                        // Starting a new episode
                        insideItem = true
                        currentTitle = null
                        currentAudioUrl = null
                    }

                    if (insideItem && tag == "title") {
                        // The next() call moves us into the text node.
                        parser.next()
                        currentTitle = parser.text?.trim()
                    }

                    if (insideItem && tag == "enclosure") {
                        // Most podcasts store the playable mp3 link here.
                        val url = parser.getAttributeValue(null, "url")
                        if (!url.isNullOrBlank()) {
                            currentAudioUrl = url
                        }
                    }
                }

                XmlPullParser.END_TAG -> {
                    val tag = parser.name.lowercase()

                    if (tag == "item") {
                        // Finished reading one episode.
                        insideItem = false

                        val title = currentTitle
                        val audio = currentAudioUrl

                        // Only add episodes that actually have playable audio.
                        if (!title.isNullOrBlank() && !audio.isNullOrBlank()) {
                            episodes.add(Episode(title = title, audioUrl = audio))
                        }
                    }
                }
            }

            eventType = parser.next()
        }

        return episodes
    }
}