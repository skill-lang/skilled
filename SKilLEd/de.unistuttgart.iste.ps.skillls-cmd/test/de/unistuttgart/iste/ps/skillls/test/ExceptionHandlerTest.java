package de.unistuttgart.iste.ps.skillls.test;

import de.unistuttgart.iste.ps.skillls.main.BreakageException;
import de.unistuttgart.iste.ps.skillls.main.ExceptionHandler;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.fail;

/**
 * Class for testing the ExceptionHandler Created on 24.02.16.
 *
 * @author Armin Hüneburg
 */
public class ExceptionHandlerTest {
	private static final ByteArrayOutputStream out = new ByteArrayOutputStream();
	private static final ByteArrayOutputStream err = new ByteArrayOutputStream();
	@BeforeClass
	public static void setup() {
		System.setOut(new PrintStream(out));
		System.setErr(new PrintStream(err));
	}

	@Test
	public void testHandle() {
		ExceptionHandler.setRethrow(true);
		try {
			ExceptionHandler.handle(new Exception("test"), "stuff");
			fail("test1 did not throw error");
		} catch (Error e) {
			// ignored
		}
		try {
			ExceptionHandler.handle(new Exception("test2"));
			fail("test2 did not throw error");
		} catch (Error e) {
			// ignored
		}
		try {
			ExceptionHandler.handle(new BreakageException(new ArrayList<>()), "stuff3");
			fail("test3 did not throw error");
		} catch (BreakageException e) {
			// ignored
		}
		try {
			ExceptionHandler.handle(new BreakageException(new ArrayList<>()));
			fail("test4 did not throw error");
		} catch (BreakageException e) {
			// ignored
		}

		ExceptionHandler.setRethrow(false);
		try {
			ExceptionHandler.handle(new Exception("test"), "stuff");
		} catch (Error e) {
			fail("test1 threw error");
		}
		try {
			ExceptionHandler.handle(new Exception("test2"));
		} catch (Error e) {
			fail("test2 threw error");
		}
		try {
			ExceptionHandler.handle(new BreakageException(new ArrayList<>()), "stuff3");
		} catch (BreakageException e) {
			fail("test3 threw error");
		}
		try {
			ExceptionHandler.handle(new BreakageException(new ArrayList<>()));
		} catch (BreakageException e) {
			fail("test4 threw error");
		}
	}
}