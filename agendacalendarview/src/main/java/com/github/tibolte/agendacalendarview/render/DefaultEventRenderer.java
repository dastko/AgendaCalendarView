package com.github.tibolte.agendacalendarview.render;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.R;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;

/**
 * Class helping to inflate our default layout in the AgendaAdapter
 */
public class DefaultEventRenderer extends EventRenderer<BaseCalendarEvent> {

    // region class - EventRenderer

    @Override
    public void render(@NonNull View view, @NonNull BaseCalendarEvent event) {
        TextView txtTitle = (TextView) view.findViewById(R.id.view_agenda_event_title);
        TextView txtLocation = (TextView) view.findViewById(R.id.view_agenda_event_location);
        TextView txtVisit = (TextView) view.findViewById(R.id.view_agenda_event_time);
        //LinearLayout descriptionContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_description_container);
        LinearLayout locationContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_location_container);
        CardView cardView = (CardView) view.findViewById(R.id.card_view);

        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frame_color);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

        if(event.getImageView() != null) {
            if (event.getImageView() != 1) {
                imageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }

        //escriptionContainer.setVisibility(View.VISIBLE);
        txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));

        txtTitle.setText(event.getTitle());
        txtVisit.setText(event.getDescription());
        if(event.getLocation().equals("")){
            cardView.setVisibility(View.GONE);
        } else {
            txtLocation.setText(event.getLocation());
        }
        frameLayout.setBackgroundColor(event.getColor());
//        if (event.getLocation().length() > 0) {
//            locationContainer.setVisibility(View.VISIBLE);
//        } else {
//            locationContainer.setVisibility(View.GONE);
//        }

//        if (event.getTitle().equals(view.getResources().getString(R.string.agenda_event_no_events))) {
//            txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));
//        } else {
//            txtTitle.setTextColor(view.getResources().getColor(R.color.theme_text_icons));

        //descriptionContainer.setBackgroundColor(event.getColor());
       // txtLocation.setTextColor(view.getResources().getColor(R.color.theme_text_icons));
    }

    @Override
    public int getEventLayout() {
        return R.layout.view_agenda_event;
    }

    // endregion
}
