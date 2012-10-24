package com.wickeria.demo;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;

import com.wickeria.demo.behavior.DelayedLoadBehavior;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;
	private final DelayedLoadBehavior behavior;

	public HomePage(final PageParameters parameters) {
		super(parameters);
		add(new RacePanel("panel"));
		behavior = new DelayedLoadBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				send(getPage(), Broadcast.DEPTH, new LoadPhotosCount(target));
			}
		};
		add(behavior);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(OnDomReadyHeaderItem.forScript(behavior.getJsTimeoutCall(Duration.milliseconds(10))));
	}

	public static class LoadPhotosCount implements Serializable {

		private static final long serialVersionUID = 1L;

		private final AjaxRequestTarget target;

		public LoadPhotosCount(AjaxRequestTarget target) {
			this.target = target;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}
	}
}
