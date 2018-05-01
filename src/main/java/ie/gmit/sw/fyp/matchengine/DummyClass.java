package ie.gmit.sw.fyp.matchengine;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component(value="dummy")
public class DummyClass extends PostEntity {
	public DummyClass() {
		properties = new HashMap<>();
	}
}
