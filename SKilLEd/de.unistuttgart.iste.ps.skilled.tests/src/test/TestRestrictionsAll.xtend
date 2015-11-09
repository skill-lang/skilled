package test

import org.eclipse.xtext.junit4.InjectWith
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import org.junit.runner.RunWith
import org.eclipse.xtext.junit4.XtextRunner
import com.google.inject.Inject
import org.eclipse.xtext.junit4.util.ParseHelper
import de.unistuttgart.iste.ps.skilled.sKilL.File
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.Assert

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestRestrictionsAll {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! restrictionsAll
			#
			#Test to check support for all restrictions at generator level; note that this
			# does not imply their correct or complete implementation.

			@unique
			Operator {
			  string name;
			}

			@monotone
			Term {

			  @nonnull
			  Operator operator;

			  Term[] arguments;
			}

			@default(System)
			@abstract
			Properties {
			}

			/* some regular property */
			RegularProperty : Properties {
			}

			/* some properties of the target system */
			@singleton
			System : Properties {
			  @nonnull
			  @constantLengthPointer
			  string name;

			  @default(1.1)
			  f32 version;
			}

			@Singleton
			None : Properties {}


			DefaultBoarderCases {
			  @default(System)
			  auto annotation system;

			  @default(0)
			  v64 nopDefault;

			  @default(-1.0)
			  @range(-1.0, 1.0)
			  f32 float;

			  @default("Hello World!")
			  string message;

			  @default(None)
			  Properties none;
			}

			RangeBoarderCases {
			  @min(0, "inclusive")
			  i8 positive;

			  @min(-1, "exclusive")
			  i16 positive2;

			  @max(0, "inclusive")
			  i32 negative;

			  @max(1, "exclusive")
			  v64 negative2;

			  @range(0.0, 360.0, "inclusive", "exclusive")
			  f32 degrees;

			  /* @note very hard to use */
			  @range(0.0, 360.0, "exclusive", "inclusive")
			  f64 degrees2;
			}

			Comment {
			  @nonnull
			  @coding("zip")
			  string text;

			  @oneOf(Operator, Term, Comment)
			  annotation target;

			  @oneOf(None, System)
			  Properties property;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}