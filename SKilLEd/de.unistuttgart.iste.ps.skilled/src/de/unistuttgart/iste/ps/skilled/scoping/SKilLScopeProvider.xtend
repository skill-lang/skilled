/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.scoping

import com.google.common.base.Predicate
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider
import org.eclipse.xtext.scoping.impl.FilteringScope
import de.unistuttgart.iste.ps.skilled.validation.ImportURIValidator

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 * 
 * @author Marco Link
 *  
 */
class SKilLScopeProvider extends AbstractDeclarativeScopeProvider {

	/**
	 * Custom scoping which takes the generated tool files into account.
	 * Root specification files cannot reference to files in the tools folder 
	 * and tools files cannot reference to something outside its folder. 
	 */
	override IScope getScope(EObject ctx, EReference ref) {
		var scope = super.getScope(ctx, ref);
		var IScope filteredScope = new FilteringScope(scope, new Predicate<IEObjectDescription>() {

			override apply(IEObjectDescription input) {
				var ctxSegments = ctx.eResource.URI.segments;
				var inputSegments = input.EObjectURI.segments;

				var boolean isCtxInTools = false;
				var boolean isInputInTools = false;

				if (ctxSegments.size() > 2) {
					if (ctxSegments.get(2).equals(ImportURIValidator::TOOLSFOLDER)) {
						isCtxInTools = true;
					}
				}

				if (inputSegments.size() > 2) {
					if (inputSegments.get(2).equals(ImportURIValidator::TOOLSFOLDER)) {
						isInputInTools = true;
					}
				}

				// One is a tool file.
				if (isInputInTools != isCtxInTools) {
					return false;
				}

				// Both are tools.
				if (isInputInTools && isCtxInTools) {
					// The files have to be in the same tools folder.
					return ctxSegments.get(3).equals(inputSegments.get(3));
				}

				return true;
			}
		});

		return filteredScope;
	}
}
