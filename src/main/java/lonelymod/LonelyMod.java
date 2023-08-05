package lonelymod;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.*;
import lonelymod.actions.ReturnToHandAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.VentFrustration;
import lonelymod.cards.cardvars.SecondDamage;
import lonelymod.cards.cardvars.SecondMagicNumber;
import lonelymod.cards.cardvars.ThirdMagicNumber;
import lonelymod.fields.CompanionField;
import lonelymod.fields.ReturnField;
import lonelymod.interfaces.TriggerOnReturnInterface;
import lonelymod.potions.CannedMeat;
import lonelymod.potions.SpecialSauce;
import lonelymod.potions.WaterFlask;
import lonelymod.relics.AbstractEasyRelic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
        OnPlayerTurnStartSubscriber {
        //PostEnergyRechargeSubscriber

    public static final String modID = "lonelymod";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public static final Color characterColor = new Color(235f/255f, 235f/255f, 52f/255f, 1f);

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
    private static final String CHARSELECT_PORTRAIT = modID + "Resources/images/charSelect/LonelyBG.png";

    private static Map<String, CompanionStrings> companionStrings = new HashMap<>();

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
        BaseMod.addDynamicVariable(new SecondDamage());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());
        new AutoAdd(modID)
                .packageFilter(AbstractEasyCard.class)
                .setDefaultSeen(true)
                .notPackageFilter("lonelymod.cards.democards.complex")
                .notPackageFilter("lonelymod.cards.democards.simple")
                .notPackageFilter("lonelymod.cards.deprecated")
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
    }

    public void receiveEditPotions() {
        BaseMod.addPotion(CannedMeat.class, BaseMod.getPotionLiquidColor(SmokeBomb.POTION_ID), BaseMod.getPotionHybridColor(SmokeBomb.POTION_ID), BaseMod.getPotionSpotsColor(SmokeBomb.POTION_ID), CannedMeat.POTION_ID, LonelyCharacter.Enums.THE_LONELY);
        BaseMod.addPotion(WaterFlask.class, BaseMod.getPotionLiquidColor(BlockPotion.POTION_ID), BaseMod.getPotionHybridColor(BlockPotion.POTION_ID), BaseMod.getPotionSpotsColor(BloodPotion.POTION_ID), WaterFlask.POTION_ID, LonelyCharacter.Enums.THE_LONELY);
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
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(modID + "Resources/localization/eng/Keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public static CompanionStrings getCompanionStrings(String companionID) {
        return companionStrings.get(companionID);
    }

    @Override
    public void receiveOnPlayerTurnStartPostDraw() {
        // This makes the return mechanic work:
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (ReturnField.willReturn.get(c)) {
                ReturnField.willReturn.set(c, false);
                AbstractDungeon.actionManager.addToBottom(new ReturnToHandAction(c));
                if (c instanceof TriggerOnReturnInterface) {
                    ((TriggerOnReturnInterface) c).triggerOnReturn();
                }
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (ReturnField.willReturn.get(c)) {
                ReturnField.willReturn.set(c, false);
                AbstractDungeon.actionManager.addToBottom(new ReturnToHandAction(c));
                if (c instanceof TriggerOnReturnInterface) {
                    ((TriggerOnReturnInterface) c).triggerOnReturn();
                }
            }
        }
        //Do this with the external method if you want it to draw BEFORE drawing the first 5 cards:
        //AbstractDungeon.player.drawPile.group.removeIf(this::tryMoveCard);
        //AbstractDungeon.player.discardPile.group.removeIf(this::tryMoveCard);
    }

    /*an external method in case you want it
    private boolean tryMoveCard(AbstractCard c) {
        if (ReturnField.willReturn.get(c)) {
            ReturnField.willReturn.set(c, false);
            AbstractDungeon.player.hand.addToHand(c);
            return true;
        }
        return false;
    }*/

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.set(AbstractDungeon.player, null);
        }
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        VentFrustration.movesCalledThisTurn = 0;
    }
}
