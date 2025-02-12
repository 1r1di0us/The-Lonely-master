package lonelymod;

import basemod.ModLabeledToggleButton;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.*;
import lonelymod.actions.ReturnAllCardsAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.cardvars.SecondBlock;
import lonelymod.cards.cardvars.SecondDamage;
import lonelymod.cards.cardvars.SecondMagicNumber;
import lonelymod.cards.cardvars.ThirdMagicNumber;
import lonelymod.fields.CompanionField;
import lonelymod.potions.CannedMeat;
import lonelymod.potions.SpecialSauce;
import lonelymod.potions.TargetPotion;
import lonelymod.relics.AbstractEasyRelic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import lonelymod.tutorials.CompanionTutorial;
import lonelymod.util.TexLoader;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class LonelyMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        OnPlayerTurnStartPostDrawSubscriber,
        PostBattleSubscriber,
        PostInitializeSubscriber,
        OnStartBattleSubscriber{
        //PostEnergyRechargeSubscriber

    public static final String modID = "lonelymod";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public static final Color characterColor = new Color(235f/255f, 235f/255f, 52f/255f, 1f);

    // mod config stuff
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String SKIP_TUTORIALS_SETTING = "enablePlaceholder";
    public static Boolean skipTutorialsPlaceholder = true; // The boolean we'll be setting on/off (true/false)
    public static ModLabeledToggleButton skipTutorials;

    //Badge thing
    private static final String MODNAME = "Lonely Mod";
    private static final String AUTHOR = "zrgrush";
    private static final String DESCRIPTION = "An exile who lives alone, and hunts with his animal companions. NL He has mostly forgotten his dark past.";
    public static final String BADGE_IMAGE = modID + "Resources/images/ui/badge.png";

    public static final String SHOULDER1 = modID + "Resources/images/char/mainChar/shoulder.png";
    public static final String SHOULDER2 = modID + "Resources/images/char/mainChar/shoulder2.png";
    public static final String CORPSE = modID + "Resources/images/char/mainChar/corpse.png";
    private static final String ATTACK_S_ART = modID + "Resources/images/512/attack.png";
    private static final String SKILL_S_ART = modID + "Resources/images/512/skill.png";
    private static final String POWER_S_ART = modID + "Resources/images/512/power.png";
    private static final String CARD_ENERGY_S = modID + "Resources/images/512/energy.png";
    private static final String TEXT_ENERGY = modID + "Resources/images/512/text_energy.png";
    private static final String ATTACK_L_ART = modID + "Resources/images/1024/attack.png";
    private static final String SKILL_L_ART = modID + "Resources/images/1024/skill.png";
    private static final String POWER_L_ART = modID + "Resources/images/1024/power.png";
    private static final String CARD_ENERGY_L = modID + "Resources/images/1024/energy.png";
    private static final String CHARSELECT_BUTTON = modID + "Resources/images/charSelect/charButton.png";
    private static final String CHARSELECT_PORTRAIT = modID + "Resources/images/charSelect/FinalBG.png";

    private static final Map<String, CompanionStrings> companionStrings = new HashMap<>();

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
    };

    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase();
            }
        }
        return "eng";
    }

    public LonelyMod() {
        BaseMod.subscribe(this);

        BaseMod.addColor(LonelyCharacter.Enums.YELLOW, characterColor,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);

        //mod config settings?
        theDefaultDefaultSettings.setProperty(SKIP_TUTORIALS_SETTING, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig(modID, makeID("Config"), theDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            skipTutorialsPlaceholder = config.getBool(SKIP_TUTORIALS_SETTING);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return modID + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    public static String makeCompanionPath(String resourcePath) { return modID + "Resources/images/companions/" + resourcePath; }

    public static void initialize() {
        LonelyMod thismod = new LonelyMod();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new LonelyCharacter(LonelyCharacter.characterStrings.NAMES[1], LonelyCharacter.Enums.THE_LONELY),
                CHARSELECT_BUTTON, CHARSELECT_PORTRAIT, LonelyCharacter.Enums.THE_LONELY);
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(AbstractEasyRelic.class)
                .any(AbstractEasyRelic.class, (info, relic) -> {
                    if (relic.color == null) {
                        BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, relic.color);
                    }
                    if (!info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());
        BaseMod.addDynamicVariable(new SecondDamage());
        BaseMod.addDynamicVariable(new SecondBlock());
        new AutoAdd(modID)
                .packageFilter(AbstractEasyCard.class)
                .setDefaultSeen(true)
                .notPackageFilter("lonelymod.cards.democards.complex")
                .notPackageFilter("lonelymod.cards.democards.simple")
                .notPackageFilter("lonelymod.cards.deprecated")
                .notPackageFilter("lonelymod.cards.summonmoves")
                .cards();
    }

    private static String makeLocPath(Settings.GameLanguage language, String filename) {
        String ret = "Resources/localization/";
        switch (language) {
            default:
                ret += "eng/";
                break;
        }
        return (modID + ret + filename + ".json");
    }

    private static String loadJson(String jsonPath)
    {
        return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
    }

    @Override
    public void receivePostInitialize() {
        receiveEditPotions();

        ModPanel settingsPanel = new ModPanel();

        skipTutorials = new ModLabeledToggleButton("Skip Tutorials",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                skipTutorialsPlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    skipTutorialsPlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(modID, makeID("Config"), theDefaultDefaultSettings);
                        config.setBool(SKIP_TUTORIALS_SETTING, skipTutorialsPlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        settingsPanel.addUIElement(skipTutorials); // Add the button to the settings panel. Button is a go.

        Texture badgeTexture = TexLoader.getTexture(BADGE_IMAGE);
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
    }

    public void receiveEditPotions() {
        BaseMod.addPotion(TargetPotion.class, BaseMod.getPotionLiquidColor(ExplosivePotion.POTION_ID), BaseMod.getPotionHybridColor(ExplosivePotion.POTION_ID), BaseMod.getPotionSpotsColor(BottledMiracle.POTION_ID), TargetPotion.POTION_ID, LonelyCharacter.Enums.THE_LONELY);
        BaseMod.addPotion(CannedMeat.class, BaseMod.getPotionLiquidColor(SmokeBomb.POTION_ID), BaseMod.getPotionHybridColor(SmokeBomb.POTION_ID), BaseMod.getPotionSpotsColor(SmokeBomb.POTION_ID), CannedMeat.POTION_ID, LonelyCharacter.Enums.THE_LONELY);
        BaseMod.addPotion(SpecialSauce.class, BaseMod.getPotionLiquidColor(AncientPotion.POTION_ID), BaseMod.getPotionHybridColor(AncientPotion.POTION_ID), BaseMod.getPotionSpotsColor(StrengthPotion.POTION_ID), SpecialSauce.POTION_ID, LonelyCharacter.Enums.THE_LONELY);

    }

    @Override
    public void receiveEditStrings() {
        //mainly from https://github.com/kiooeht/Bard/blob/abe81400f0aa8305db8ade09ed26204d40ba2250/src/main/java/com/evacipated/cardcrawl/mod/bard/BardMod.java#L359
        //and bard/helpers/MelodyManager.loadMelodyStrings
        Gson gson = new Gson();
        Type companionType = new TypeToken<Map<String, CompanionStrings>>(){}.getType();
        Map<String, CompanionStrings> map = gson.fromJson(loadJson(makeLocPath(Settings.language, "Companionstrings")), companionType);
        companionStrings.putAll(map);

        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocPath(Settings.language, "Cardstrings"));

        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocPath(Settings.language, "Relicstrings"));

        BaseMod.loadCustomStringsFile(OrbStrings.class, makeLocPath(Settings.language, "Orbstrings"));

        BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocPath(Settings.language, "Charstrings"));

        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(Settings.language, "Potionstrings"));

        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocPath(Settings.language, "Powerstrings"));

        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocPath(Settings.language, "uistrings"));

        BaseMod.loadCustomStringsFile(TutorialStrings.class, makeLocPath(Settings.language, "Tutorialstrings"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(modID + "Resources/localization/eng/Keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);



        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                if (keyword.ID != null) {
                    KeywordManager.KEYWORDS.put(keyword.ID, keyword);
                }
            }
        }
    }

    public static CompanionStrings getCompanionStrings(String companionID) {
        return companionStrings.get(companionID);
    }

    @Override
    public void receiveOnPlayerTurnStartPostDraw() {
        // This makes the return mechanic work:
        AbstractDungeon.actionManager.addToBottom(new ReturnAllCardsAction(true));

        //Just in case for when target runs out on Omen, but its default move has already been called so yeah.
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.get(AbstractDungeon.player).getTarget();
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.set(AbstractDungeon.player, null);
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if (!skipTutorials.toggle.enabled && AbstractDungeon.player.chosenClass.equals(LonelyCharacter.Enums.THE_LONELY)){
            AbstractDungeon.ftue = new CompanionTutorial();
            skipTutorials.toggle.toggle();

        }
    }
}
