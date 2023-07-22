package lonelymod.patches;


import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import javassist.*;
import org.clapper.util.classutil.*;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;

//this doesn't matter?
//nope. its all useless.
//but I'm keeping it here so I can look back and remember how i did it.
@SpirePatch( 
                clz=CardCrawlGame.class,
                method=SpirePatch.CONSTRUCTOR
)
public class ManualDiscardDynamicPatch {
    

    public static void Raw(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
        ClassFinder finder = new ClassFinder();
        finder.add(
                        new File(Loader.STS_JAR)
        );
        // search through every card class, except this only does modded ones?
        finder.add(
                        Arrays.stream(Loader.MODINFOS)
                        .map(modInfo -> modInfo.jarURL)
                        .filter(Objects::nonNull)
                        .map(url -> {
                            try {
                                return url.toURI();
                            } catch (URISyntaxException e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .map(File::new)
                        .collect(Collectors.toList())
        );
        ClassPool pool = ctBehavior.getDeclaringClass().getClassPool();

		ClassFilter filter =
				new AndClassFilter(
						new NotClassFilter(new InterfaceOnlyClassFilter()),
						new NotClassFilter(new AbstractClassFilter()),
						new ClassModifiersClassFilter(Modifier.PUBLIC),
						new SuperClassFilter(pool, AbstractCard.class)
				);
        List<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        String src = "super.triggerOnManualDiscard();";

        for (ClassInfo classInfo : foundClasses) {
			try {
				CtMethod initializeData = null;
				CtClass ctClass = pool.get(classInfo.getClassName());
				try {
					initializeData = ctClass.getDeclaredMethod("triggerOnManualDiscard");
				} catch (NotFoundException ignored) {
				}

				// If the card defines triggerOnManualDiscard(), postfix it
				if (initializeData != null) {
					initializeData.insertBefore(src);
				}// Otherwise, postfix all of its constructors
                //else {
				//	for (CtConstructor ctor : ctClass.getDeclaredConstructors()) {
				//		ctor.insertAfter(src);
				//	}
				//}
			} catch (NotFoundException ignored) {
			}
		}
    }


    private static class SuperClassFilter implements ClassFilter
	{
		private ClassPool pool;
		private CtClass baseClass;

		public SuperClassFilter(ClassPool pool, Class<?> baseClass) throws NotFoundException
		{
			this.pool = pool;
			this.baseClass = pool.get(baseClass.getName());
		}

		@Override
		public boolean accept(ClassInfo classInfo, ClassFinder classFinder)
		{
			try {
				CtClass ctClass = pool.get(classInfo.getClassName());
				while (ctClass != null) {
					if (ctClass.equals(baseClass)) {
						return true;
					}
					ctClass = ctClass.getSuperclass();
				}
			} catch (NotFoundException ignored) {
			}

			return false;
		}
	}
}
