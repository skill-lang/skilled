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
class TestHints {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! hintsAll
			# This specification applies all known hints to some fields.
			# The test is in fact a mere compilability test.

			/** A user has a name and an age. */
			!owner(HintOwner)
			!monotone
			User {

			  /**
			   * Full name of the user ignoring regional specifics like give or family
			   * names.
			   * @note no one wants to know your name, until you are in trouble
			   */
			  !onDemand
			  string name;

			 /** 
			  The current age of the user.
			  @note nobody cares for your age
			  */
			  !distributed
			  v64 age;

			  /** this field can only be seen by reflection */
			  !hide
			  string reflectivelyVisible;
			}

			/** Just for fun */
			!Owner(DebugHintOwner)
			Abuser {
			  /** provided by debug, case insensitity check */
			  !provider(debughintOwner)
			  string abuseDescription;
			}

			/**
			 what ever it was before, now it is a singleton
			 @todo provide a test binary to check this hint (where it should be abstract; and a fail, where it has a subclass,  because it can not be a singleton in that case)
			 @note this is readOnly; should not matter, because it has no mutable state
			 */
			!removeUnknownRestrictions
			!readOnly
			@Singleton 
			NowASingleton {

			 /**
			  @todo provide test files with guards 0, 1 and 0xCAFE
			  */
			  !constantMutator(0,1)
			  const i16 guard = 0xABCD;
			}

			/**
			 A type mixed into our hirarchy.
			 @todo provide tests for programming languages using actual user defined implementations
			 */
			!mixin
			ExternMixin {
			  /** what ever it is */
			  annotation unknownStuff;
			}

			!ignore
			BadType {
			  string ignoredData;

			  /** this field should not be accessible, because the type itself is ignored */
			  !hide
			  string reflectivelyInVisible;
			}

			/** Unique Identifiers are unique and appear as if they were longs */
			!flat
			!unique
			@unique
			UID {
			  i64 identifier;
			}

			/** all expressions are pure */
			!pure
			!pragma tree is a funny extension that is likely to be ignored in this form
			@abstract
			Expression {
			}		
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}