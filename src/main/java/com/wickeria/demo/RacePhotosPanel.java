package com.wickeria.demo;

import java.util.Random;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.time.Duration;

import com.wickeria.demo.HomePage.LoadPhotosCount;
import com.wickeria.demo.RacePanel.RaceRow;
import com.wickeria.demo.behavior.DelayedLoadBehavior;

public class RacePhotosPanel extends GenericPanel<RaceRow> {

	private static final long serialVersionUID = 1L;
	private static final String PHOTOS_ICON = "<span class=\"photos-icon\"></span>";
	private static final Random RANDOM = new Random();
	private final WebMarkupContainer wmc;
	private int numberOfPhotos = 0;

	public RacePhotosPanel(String id, IModel<RaceRow> model) {
		super(id, model);
		wmc = new WebMarkupContainer("wmc");
		wmc.setOutputMarkupId(true);
		WebMarkupContainer link = new WebMarkupContainer("link") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.setName("span");
				super.onComponentTag(tag);
			}
		};
		Image count = new Image("count", new PackageResourceReference(DelayedLoadBehavior.class, "ajax-loader.gif")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.setName("img");
				super.onComponentTag(tag);
			}
		};
		add(wmc.add(link.add(count)));
	}

	private void updatePhotosCount(AjaxRequestTarget target) {
		countNumberOfPhotos();
		Component link = wmc.get("link");
		if (numberOfPhotos > 0) {
			AjaxLink<Void> openLink = new AjaxLink<Void>("link") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					target.appendJavaScript("alert('modální okno s fotkami..');");
				}
			};
			openLink.add(new Label("count", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return PHOTOS_ICON + numberOfPhotos;
				}
			}).setRenderBodyOnly(true).setEscapeModelStrings(false));
			link.replaceWith(openLink);
		} else {
			link.get("count").replaceWith(new Label("count", "0"));
		}
		target.add(wmc);
	}

	private void countNumberOfPhotos() {
		// RaceRow raceRow = getModelObject();
		// call service which can count number of photos (long-standing operation)
		Duration.milliseconds(500).sleep();
		numberOfPhotos = RANDOM.nextInt(5);
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if (event.getPayload() instanceof LoadPhotosCount) {
			LoadPhotosCount loadPhotosCount = (LoadPhotosCount) event.getPayload();
			updatePhotosCount(loadPhotosCount.getTarget());
		}
	}
}
