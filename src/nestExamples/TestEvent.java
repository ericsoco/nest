package nestExamples;

import com.transmote.nest.events.Event;

public class TestEvent extends Event {
	public static final int TEST_EVENT_TYPE = 10;
	
	public TestEvent (int type) {
		super(type);
	}
}