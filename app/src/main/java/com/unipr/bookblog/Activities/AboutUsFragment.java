package com.unipr.bookblog.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unipr.bookblog.Models.AboutUs;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import lombok.NoArgsConstructor;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

@NoArgsConstructor
public class AboutUsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        XmlPullParserFactory pullParserFactory;
        AboutUs admin;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = requireActivity().getApplicationContext().getAssets().open("sample.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            ArrayList<AboutUs> admins = parseXML(parser);
            admin = admins.get(0);

            assert admin != null;
            View aboutPage = new AboutPage(getContext())
                    .isRTL(false)
                    .setDescription(admin.getDescription())
                    .addItem(new Element().setTitle(admin.getItem()))
                    .addGroup(admin.getGroup())
                    .addEmail(admin.getEmail())
                    .addWebsite(admin.getWebsite())
                    .addYoutube(admin.getYoutube())
                    .addPlayStore(admin.getPlayStore())
                    .addInstagram(admin.getInstagram())
                    .addItem(createCopyright())
                    .create();
            return aboutPage;
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<AboutUs> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<AboutUs> admins = null;
        int eventType = parser.getEventType();
        AboutUs admin = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    admins = new ArrayList<>();
                    break;

                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("admins")) {
                        admin = new AboutUs();
                        admin.setId(parser.getAttributeValue(null, "id"));
                    } else if (admin != null) {
                        switch (name) {
                            case "description":
                                admin.setDescription(parser.nextText());
                                break;
                            case "item":
                                admin.setItem(parser.nextText());
                                break;
                            case "group":
                                admin.setGroup(parser.nextText());
                                break;
                            case "email":
                                admin.setEmail(parser.nextText());
                                break;
                            case "website":
                                admin.setWebsite(parser.nextText());
                                break;
                            case "youtube":
                                admin.setYoutube(parser.nextText());
                                break;
                            case "playstore":
                                admin.setPlayStore(parser.nextText());
                                break;
                            case "instagram":
                                admin.setInstagram(parser.nextText());
                                break;
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("admin") && admin != null) {
                        assert admins != null;
                        admins.add(admin);
                    }
            }
            eventType = parser.next();
        }

        return admins;
    }

    private Element createCopyright() {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Bookism", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(v -> Toast.makeText(getActivity(), copyrightString, Toast.LENGTH_SHORT).show());
        return copyright;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}