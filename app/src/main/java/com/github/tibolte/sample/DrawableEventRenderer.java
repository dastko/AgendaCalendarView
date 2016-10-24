package com.github.tibolte.sample;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.render.EventRenderer;

public class DrawableEventRenderer extends EventRenderer<DrawableCalendarEvent> {

    // region Class - EventRenderer

    @Override
    public void render(View view, DrawableCalendarEvent event) {
        TextView txtTitle = (TextView) view.findViewById(com.github.tibolte.agendacalendarview.R.id.view_agenda_event_title);
        TextView txtLocation = (TextView) view.findViewById(com.github.tibolte.agendacalendarview.R.id.view_agenda_event_location);
        //LinearLayout descriptionContainer = (LinearLayout) view.findViewById(com.github.tibolte.agendacalendarview.R.id.view_agenda_event_description_container);
        LinearLayout locationContainer = (LinearLayout) view.findViewById(com.github.tibolte.agendacalendarview.R.id.view_agenda_event_location_container);
        CardView cardView = (CardView) view.findViewById(R.id.card_view);
        ImageView imageView = (ImageView) view.findViewById(R.id.transition_image);

        if(event.getImageView() != null) {
            if (event.getImageView() != 1) {
                imageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            imageView.setImageDrawable(view.get(R.drawable.ic_check_circle_black_24dp, getApplicationContext().getTheme()));
//        } else {
//            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle_black_24dp));
//        }

        //descriptionContainer.setVisibility(View.VISIBLE);


        txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));
//        Drawable drawable = imageView.getDrawable();
//        if(drawable != null)
//            imageView.setImageDrawable(drawable);

        txtTitle.setText(event.getTitle());
        txtLocation.setText(event.getLocation());
        if (event.getLocation().length() > 0) {
            locationContainer.setVisibility(View.VISIBLE);
            txtLocation.setText(event.getLocation());
        } else {
            locationContainer.setVisibility(View.GONE);
        }

        if (event.getTitle().equals(view.getResources().getString(com.github.tibolte.agendacalendarview.R.string.agenda_event_no_events))) {
            txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));
            cardView.setVisibility(View.GONE);
        } else {
            txtTitle.setTextColor(view.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));
        }
        //descriptionContainer.setBackgroundColor(event.getColor());
        txtLocation.setTextColor(view.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));
    }

    @Override
    public int getEventLayout() {
        return R.layout.view_agenda_drawable_event;
    }

    @Override
    public Class<DrawableCalendarEvent> getRenderType() {
        return DrawableCalendarEvent.class;
    }

    // endregion
}
