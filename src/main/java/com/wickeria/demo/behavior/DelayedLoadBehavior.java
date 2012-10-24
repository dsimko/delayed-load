package com.wickeria.demo.behavior;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxChannel;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.util.time.Duration;

/**
 * Behavior which allows AJAX request after page is loaded.
 * 
 * <p>
 * Example:
 * 
 * <pre>
 * public class SomePanel extends Panel {
 * 	private final DelayedLoadBehavior behavior;
 * 
 * 	public SomePanel(String id) {
 * 		super(id);
 * 		behavior = new DelayedLoadBehavior() {
 * 
 * 			protected void respond(AjaxRequestTarget target) {
 * 				// send some event
 * 			}
 * 		};
 * 		add(behavior);
 * 	}
 * 
 * 	public void renderHead(IHeaderResponse response) {
 * 		response.render(OnDomReadyHeaderItem.forScript(behavior.getJsTimeoutCall(Duration.seconds(1))));
 * 	}
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author dan
 * 
 */

public abstract class DelayedLoadBehavior extends AbstractDefaultAjaxBehavior {

	private static final long serialVersionUID = 1L;

	public String getJsTimeoutCall(final Duration updateInterval) {
		CharSequence js = getCallbackScript();
		js = JavaScriptUtils.escapeQuotes(js);
		String timeoutHandle = getTimeoutHandle();
		return timeoutHandle + " = setTimeout('" + js + "', " + updateInterval.getMilliseconds() + ')';
	}

	protected String getTimeoutHandle() {
		return "delayLoadHandler" + getComponent().getMarkupId();
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		// this will remove ajax requests serialization at the client side (JavaScript)
		attributes.setChannel(new AjaxChannel(DelayedLoadBehavior.class.getSimpleName()));
	}

}
