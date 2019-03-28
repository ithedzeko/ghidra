/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.framework.plugintool;

import ghidra.framework.model.ToolConnection;

/**
 * Event generated by a plugin.
 * <p>
 * A PluginEvent should be annotate with a {@link ToolEventName} if it may be
 * passed between multiple tools via a {@link ToolConnection}.
 */
public abstract class PluginEvent {
	
	/**
	 * Name of event source when plugin event is passed to
	 * another tool as cross-tool event.
	 */
	public static final String EXTERNAL_SOURCE_NAME = "External Tool";

	private String eventName;
	private String sourceName;
	private PluginEvent triggerEvent;

	/**
	 * Returns the tool event name corresponding to the given pluginEventClass.
	 * If no corresponding tool event exists, null will be returned.
	 */
	public static String lookupToolEventName(Class<?> pluginEventClass) {
		ToolEventName eventNameAnnotation = pluginEventClass.getAnnotation(ToolEventName.class);
		if (eventNameAnnotation != null) {
			return eventNameAnnotation.value();
		}
		return null;
	}

	/**
	 * Constructor
	 * @param sourceName source name of the event
	 * @param eventName name of event
	 */
	protected PluginEvent(String sourceName, String eventName) {
		this.eventName = eventName;
		this.sourceName = sourceName;
	}
	
	/**
	 * Determine if this event has been annotated with a {@link ToolEventName} which
	 * makes it available for passing to another tool via a {@link ToolConnection}.
	 * @return true if event can be utilized as a cross-tool event
	 */
	public boolean isToolEvent() {
		return getToolEventName() != null;
	}
	
	/** 
	 * Get the optional cross-tool event name which has been established via
	 * a {@link ToolEventName} annotation which makes it available for
	 * passing as an external tool via a {@link ToolConnection}.
	 * This name may differ from the {@link #getEventName()}.s
	 * @return tool event name or null if not permitted as a cross-tool event
	 */
    public final String getToolEventName() {
    		return lookupToolEventName(getClass());
    }

	/**
	 * Get the plugin event name.
	 */
	public final String getEventName() {
		return eventName;
	}

	/**
	 * Returns the name of the plugin immediately responsible for firing this
	 * event.
	 */
	public final String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String s) {
		sourceName = s;
	}

	public void setTriggerEvent(PluginEvent triggerEvent) {
		this.triggerEvent = triggerEvent;
	}

	public PluginEvent getTriggerEvent() {
		return triggerEvent;
	}

	@Override
	public String toString() {
		String details = getDetails();
		StringBuilder builder = new StringBuilder();
		builder.append("Event: ");
		builder.append(eventName);
		builder.append("  Source: ");
		builder.append(sourceName);
		if (details != null) {
			builder.append("\n\tDetails: ");
			builder.append(details);
		}
		return builder.toString();
	}

	protected String getDetails() {
		return null;
	}
}