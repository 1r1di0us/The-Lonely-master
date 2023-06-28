package lonelymod.companions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.ShieldParticleEffect;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashIntentEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import com.megacrit.cardcrawl.vfx.combat.StunStarEffect;
import com.megacrit.cardcrawl.vfx.combat.UnknownParticleEffect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class AbstractCompanion extends AbstractCreature {
    private static final Logger logger = LogManager.getLogger(AbstractCompanion.class.getName());

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("AbstractMonster"); //huh?
    public static final String[] TEXT = uiStrings.TEXT;

    private static final float DEATH_TIME = 1.8F;
    private static final float ESCAPE_TIME = 3.0F;
    protected static final byte ESCAPE = 99;
    protected static final byte ROLL = 98;
  
    public float deathTimer = 0.0F;
  
    private Color nameColor = new Color();
    private Color nameBgColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
  
    protected Texture img;
  
    public boolean tintFadeOutCalled = false;
  
    protected HashMap<Byte, String> moveSet = new HashMap<>();
  
    public boolean escaped = false;
    public boolean escapeNext = false;
  
    private PowerTip intentTip = new PowerTip();
        
    private float hoverTimer = 0.0F;
    
    public boolean cannotEscape = false;
    
    public ArrayList<DamageInfo> damage = new ArrayList<>();
    
    private CompanionMoveInfo move;
    
    private float intentParticleTimer = 0.0F;
    private float intentAngle = 0.0F;
    
    public ArrayList<Byte> moveHistory = new ArrayList<>();
    
    private ArrayList<AbstractGameEffect> intentVfx = new ArrayList<>();
    
    public byte nextMove = -1;
    
    private static final int INTENT_W = 128;
    
    private BobEffect bobEffect = new BobEffect();
    
    private static final float INTENT_HB_W = 64.0F * Settings.scale;
    public Hitbox intentHb;
    public Intent intent = Intent.DEBUG;
    public Intent tipIntent = Intent.DEBUG;
    
    public float intentAlpha = 0.0F;
    
    public float intentAlphaTarget = 0.0F;
    
    public float intentOffsetX = 0.0F;
    
    private Texture intentImg = null;
    private Texture intentBg = null;

    private int intentDmg = -1;
    private int intentBaseDmg = -1;
    private int intentMultiAmt = 0;
    private boolean isMultiDmg = false;
    
    private Color intentColor = Color.WHITE.cpy();
    
    public String moveName = null;
    
    protected List<Disposable> disposables = new ArrayList<>();
    
    public static String[] MOVES;
    public static String[] DIALOG;

    public static Comparator<AbstractMonster> sortByHitbox; //no?

    public enum Intent {
        ATTACK, ATTACK_BUFF, ATTACK_DEBUFF, ATTACK_DEFEND, BUFF, DEBUFF, STRONG_DEBUFF, DEBUG, DEFEND, DEFEND_DEBUFF, DEFEND_BUFF, ESCAPE, MAGIC, NONE, SLEEP, STUN, UNKNOWN;
    }

    static {
        sortByHitbox = ((o1, o2) -> (int)(o1.hb.cX - o2.hb.cX));
    }

    public AbstractCompanion(String name, String id, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        this.isPlayer = false;
        this.name = name;
        this.id = id;
        if (Settings.isMobile)
            hb_w *= 1.17F; 
        this.drawX = Settings.WIDTH * 0.75F + offsetX * Settings.xScale;
        this.drawY = AbstractDungeon.floorY + offsetY * Settings.yScale;
        this.hb_w = hb_w * Settings.scale;
        this.hb_h = hb_h * Settings.xScale;
        this.hb_x = hb_x * Settings.scale;
        this.hb_y = hb_y * Settings.scale;
        if (imgUrl != null)
            this.img = ImageMaster.loadImage(imgUrl);
        this.intentHb = new Hitbox(INTENT_HB_W, INTENT_HB_W);
        this.hb = new Hitbox(this.hb_w, this.hb_h);
        refreshHitboxLocation();
        refreshIntentHbLocation();
    }

    public AbstractCompanion(String name, String id, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        this(name, id, hb_x, hb_y, hb_w, hb_h, imgUrl, 0.0F, 0.0F);
    }

    public void refreshIntentHbLocation() {
        this.intentHb.move(this.hb.cX + this.intentOffsetX, this.hb.cY + this.hb_h / 2.0F + INTENT_HB_W / 2.0F);
    }
  
    public void update() {
        for (AbstractPower p : this.powers)
            p.updateParticles(); 
            updateReticle();
            updateHealthBar();
            updateAnimations();
            updateDeathAnimation();
            updateEscapeAnimation();
            updateIntent();
            this.tint.update();
    }

    private void updateIntent() {
        this.bobEffect.update();
        if (this.intentAlpha != this.intentAlphaTarget && this.intentAlphaTarget == 1.0F) {
            this.intentAlpha += Gdx.graphics.getDeltaTime();
            if (this.intentAlpha > this.intentAlphaTarget)
                this.intentAlpha = this.intentAlphaTarget; 
        } else if (this.intentAlphaTarget == 0.0F) {
            this.intentAlpha -= Gdx.graphics.getDeltaTime() / 1.5F;
            if (this.intentAlpha < 0.0F)
                this.intentAlpha = 0.0F; 
        } 
        if (!this.isDying && !this.isEscaping)
            updateIntentVFX(); 
        for (Iterator<AbstractGameEffect> i = this.intentVfx.iterator(); i.hasNext(); ) {
            AbstractGameEffect e = i.next();
            e.update();
            if (e.isDone)
                i.remove(); 
        } 
    }
  
    private void updateIntentVFX() {
        if (this.intentAlpha > 0.0F)
        if (this.intent == Intent.ATTACK_DEBUFF || this.intent == Intent.DEBUFF || this.intent == Intent.STRONG_DEBUFF || this.intent == Intent.DEFEND_DEBUFF) {
            this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
            if (this.intentParticleTimer < 0.0F) {
                this.intentParticleTimer = 1.0F;
                this.intentVfx.add(new DebuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
            } 
        } else if (this.intent == Intent.ATTACK_BUFF || this.intent == Intent.BUFF || this.intent == Intent.DEFEND_BUFF) {
            this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
            if (this.intentParticleTimer < 0.0F) {
                this.intentParticleTimer = 0.1F;
                this.intentVfx.add(new BuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
            } 
        } else if (this.intent == Intent.ATTACK_DEFEND) {
            this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
            if (this.intentParticleTimer < 0.0F) {
                this.intentParticleTimer = 0.5F;
                this.intentVfx.add(new ShieldParticleEffect(this.intentHb.cX, this.intentHb.cY));
            } 
        } else if (this.intent == Intent.UNKNOWN) {
            this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
            if (this.intentParticleTimer < 0.0F) {
                this.intentParticleTimer = 0.5F;
                this.intentVfx.add(new UnknownParticleEffect(this.intentHb.cX, this.intentHb.cY));
            } 
        } else if (this.intent == Intent.STUN) {
            this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
            if (this.intentParticleTimer < 0.0F) {
                this.intentParticleTimer = 0.67F;
                this.intentVfx.add(new StunStarEffect(this.intentHb.cX, this.intentHb.cY));
            } 
        }  
    }
  
    public void renderTip(SpriteBatch sb) {
        this.tips.clear();
        if (this.intentAlphaTarget == 1.0F && !AbstractDungeon.player.hasRelic("Runic Dome") && this.intent != Intent.NONE)
            this.tips.add(this.intentTip); 
        for (AbstractPower p : this.powers) {
            if (p.region48 != null) {
                this.tips.add(new PowerTip(p.name, p.description, p.region48));
                continue;
            } 
            this.tips.add(new PowerTip(p.name, p.description, p.img));
        } 
        if (!this.tips.isEmpty())
            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + 
                                        TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            } else {
                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY + 
                                        TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            }  
    }

    private void updateIntentTip() {
        switch (this.intent) {
            case ATTACK:
                this.intentTip.header = TEXT[0];
                if (this.isMultiDmg) {
                    this.intentTip.body = TEXT[1] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[3];
                } else {
                    this.intentTip.body = TEXT[4] + this.intentDmg + TEXT[5];
                } 
                this.intentTip.img = getAttackIntentTip();
                return;
            case ATTACK_BUFF:
                this.intentTip.header = TEXT[6];
                if (this.isMultiDmg) {
                    this.intentTip.body = TEXT[7] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[8];
                } else {
                    this.intentTip.body = TEXT[9] + this.intentDmg + TEXT[5];
                } 
                this.intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
                return;
            case ATTACK_DEBUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[11] + this.intentDmg + TEXT[5];
                this.intentTip.img = ImageMaster.INTENT_ATTACK_DEBUFF;
                return;
            case ATTACK_DEFEND:
                this.intentTip.header = TEXT[0];
                if (this.isMultiDmg) {
                    this.intentTip.body = TEXT[12] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[3];
                } else {
                    this.intentTip.body = TEXT[12] + this.intentDmg + TEXT[5];
                } 
                    this.intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
                return;
            case BUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[19];
                this.intentTip.img = ImageMaster.INTENT_BUFF;
                return;
            case DEBUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[20];
                this.intentTip.img = ImageMaster.INTENT_DEBUFF;
                return;
            case STRONG_DEBUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[21];
                this.intentTip.img = ImageMaster.INTENT_DEBUFF2;
                return;
            case DEFEND:
                this.intentTip.header = TEXT[13];
                this.intentTip.body = TEXT[22];
                this.intentTip.img = ImageMaster.INTENT_DEFEND;
                return;
            case DEFEND_DEBUFF:
                this.intentTip.header = TEXT[13];
                this.intentTip.body = TEXT[23];
                this.intentTip.img = ImageMaster.INTENT_DEFEND;
                return;
            case DEFEND_BUFF:
                this.intentTip.header = TEXT[13];
                this.intentTip.body = TEXT[24];
                this.intentTip.img = ImageMaster.INTENT_DEFEND_BUFF;
                return;
            case ESCAPE:
                this.intentTip.header = TEXT[14];
                this.intentTip.body = TEXT[25];
                this.intentTip.img = ImageMaster.INTENT_ESCAPE;
                return;
            case MAGIC:
                this.intentTip.header = TEXT[15];
                this.intentTip.body = TEXT[26];
                this.intentTip.img = ImageMaster.INTENT_MAGIC;
                return;
            case SLEEP:
                this.intentTip.header = TEXT[16];
                this.intentTip.body = TEXT[27];
                this.intentTip.img = ImageMaster.INTENT_SLEEP;
                return;
            case STUN:
                this.intentTip.header = TEXT[17];
                this.intentTip.body = TEXT[28];
                this.intentTip.img = ImageMaster.INTENT_STUN;
                return;
            case UNKNOWN:
                this.intentTip.header = TEXT[18];
                this.intentTip.body = TEXT[29];
                this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
                return;
            case NONE:
                this.intentTip.header = "";
                this.intentTip.body = "";
                this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
                return;
        } 
        this.intentTip.header = "NOT SET";
        this.intentTip.body = "NOT SET";
        this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
    }

    public void flashIntent() {
        if (this.intentImg != null)
            AbstractDungeon.effectList.add(new FlashCompanionIntentEffect(this.intentImg, this)); 
        this.intentAlphaTarget = 0.0F;
    }
  
    public void createIntent() {
        this.intent = this.move.intent;
        this.intentParticleTimer = 0.5F;
        this.nextMove = this.move.nextMove;
        this.intentBaseDmg = this.move.baseDamage;
        if (this.move.baseDamage > -1) {
            calculateDamage(this.intentBaseDmg);
            if (this.move.isMultiDamage) {
                this.intentMultiAmt = this.move.multiplier;
                this.isMultiDmg = true;
            } else {
                this.intentMultiAmt = -1;
                this.isMultiDmg = false;
            } 
        } 
        this.intentImg = getIntentImg();
        this.intentBg = getIntentBg();
        this.tipIntent = this.intent;
        this.intentAlpha = 0.0F;
        this.intentAlphaTarget = 1.0F;
        updateIntentTip();
    }
  
    public void setMove(String moveName, byte nextMove, Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
        this.moveName = moveName;
        if (nextMove != -1)
            this.moveHistory.add(Byte.valueOf(nextMove)); 
        this.move = new CompanionMoveInfo(nextMove, intent, baseDamage, multiplier, isMultiDamage);
    }
    
    public void setMove(byte nextMove, Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
        setMove((String)null, nextMove, intent, baseDamage, multiplier, isMultiDamage);
    }
    
    public void setMove(byte nextMove, Intent intent, int baseDamage) {
        setMove((String)null, nextMove, intent, baseDamage, 0, false);
    }
    
    public void setMove(String moveName, byte nextMove, Intent intent, int baseDamage) {
        setMove(moveName, nextMove, intent, baseDamage, 0, false);
    }
  
    public void setMove(String moveName, byte nextMove, Intent intent) {
        if (intent == Intent.ATTACK || intent == Intent.ATTACK_BUFF || intent == Intent.ATTACK_DEFEND || intent == Intent.ATTACK_DEBUFF) {
        for (int i = 0; i < 8; i++)
            AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(
                
                MathUtils.random(Settings.WIDTH * 0.25F, Settings.WIDTH * 0.75F), 
                MathUtils.random(Settings.HEIGHT * 0.25F, Settings.HEIGHT * 0.75F), "COMPANION MOVE " + moveName + " IS SET INCORRECTLY! REPORT TO DEV", Color.RED
                
                .cpy())); 
            logger.info("COMPANION MOVE " + moveName + " IS SET INCORRECTLY! REPORT TO DEV");
        } 
        setMove(moveName, nextMove, intent, -1, 0, false);
    }
  
    public void setMove(byte nextMove, Intent intent) {
        setMove((String)null, nextMove, intent, -1, 0, false);
    }

    private Texture getIntentImg() {
        switch (this.intent) {
            case ATTACK:
                return getAttackIntent();
            case ATTACK_BUFF:
                return getAttackIntent();
            case ATTACK_DEBUFF:
                return getAttackIntent();
            case ATTACK_DEFEND:
                return getAttackIntent();
            case BUFF:
                return ImageMaster.INTENT_BUFF_L;
            case DEBUFF:
                return ImageMaster.INTENT_DEBUFF_L;
            case STRONG_DEBUFF:
                return ImageMaster.INTENT_DEBUFF2_L;
            case DEFEND:
                return ImageMaster.INTENT_DEFEND_L;
            case DEFEND_DEBUFF:
                return ImageMaster.INTENT_DEFEND_L;
            case DEFEND_BUFF:
                return ImageMaster.INTENT_DEFEND_BUFF_L;
            case ESCAPE:
                return ImageMaster.INTENT_ESCAPE_L;
            case MAGIC:
                return ImageMaster.INTENT_MAGIC_L;
            case SLEEP:
                return ImageMaster.INTENT_SLEEP_L;
            case STUN:
                return null;
            case UNKNOWN:
                return ImageMaster.INTENT_UNKNOWN_L;
        } 
        return ImageMaster.INTENT_UNKNOWN_L;
    }
  
    private Texture getIntentBg() {
        switch (this.intent) {
            case ATTACK_DEFEND:
                return null;
        } 
        return null;
    }
  
    protected Texture getAttackIntent(int dmg) {
        if (dmg < 5)
            return ImageMaster.INTENT_ATK_1; 
        if (dmg < 10)
            return ImageMaster.INTENT_ATK_2; 
        if (dmg < 15)
            return ImageMaster.INTENT_ATK_3; 
        if (dmg < 20)
            return ImageMaster.INTENT_ATK_4; 
        if (dmg < 25)
            return ImageMaster.INTENT_ATK_5; 
        if (dmg < 30)
            return ImageMaster.INTENT_ATK_6; 
        return ImageMaster.INTENT_ATK_7;
    }
  
    protected Texture getAttackIntent() {
        int tmp;
        if (this.isMultiDmg) {
            tmp = this.intentDmg * this.intentMultiAmt;
        } else {
            tmp = this.intentDmg;
        } 
        if (tmp < 5)
            return ImageMaster.INTENT_ATK_1; 
        if (tmp < 10)
            return ImageMaster.INTENT_ATK_2; 
        if (tmp < 15)
            return ImageMaster.INTENT_ATK_3; 
        if (tmp < 20)
            return ImageMaster.INTENT_ATK_4; 
        if (tmp < 25)
            return ImageMaster.INTENT_ATK_5; 
        if (tmp < 30)
            return ImageMaster.INTENT_ATK_6; 
        return ImageMaster.INTENT_ATK_7;
    }
  
    private Texture getAttackIntentTip() {
        int tmp;
        if (this.isMultiDmg) {
            tmp = this.intentDmg * this.intentMultiAmt;
        } else {
            tmp = this.intentDmg;
        } 
        if (tmp < 5)
            return ImageMaster.INTENT_ATK_TIP_1; 
        if (tmp < 10)
            return ImageMaster.INTENT_ATK_TIP_2; 
        if (tmp < 15)
            return ImageMaster.INTENT_ATK_TIP_3; 
        if (tmp < 20)
            return ImageMaster.INTENT_ATK_TIP_4; 
        if (tmp < 25)
            return ImageMaster.INTENT_ATK_TIP_5; 
        if (tmp < 30)
            return ImageMaster.INTENT_ATK_TIP_6; 
        return ImageMaster.INTENT_ATK_TIP_7;
    }

    public void damage(DamageInfo info) {
        /*if (info.output > 0 && hasPower("IntangiblePlayer"))
            info.output = 1; 
        int damageAmount = info.output;
        if (this.isDying || this.isEscaping)
            return; 
        if (damageAmount < 0)
            damageAmount = 0; 
        boolean hadBlock = true;
        if (this.currentBlock == 0)
            hadBlock = false; 
        boolean weakenedToZero = (damageAmount == 0);
        damageAmount = decrementBlock(info, damageAmount);
        if (info.owner == AbstractDungeon.player)
            for (AbstractRelic r : AbstractDungeon.player.relics)
                damageAmount = r.onAttackToChangeDamage(info, damageAmount);  
        if (info.owner != null)
            for (AbstractPower p : info.owner.powers)
                damageAmount = p.onAttackToChangeDamage(info, damageAmount);  
        for (AbstractPower p : this.powers)
            damageAmount = p.onAttackedToChangeDamage(info, damageAmount); 
        if (info.owner == AbstractDungeon.player)
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onAttack(info, damageAmount, this);  
        for (AbstractPower p : this.powers)
            p.wasHPLost(info, damageAmount); 
        if (info.owner != null)
            for (AbstractPower p : info.owner.powers)
                p.onAttack(info, damageAmount, this);  
        for (AbstractPower p : this.powers)
            damageAmount = p.onAttacked(info, damageAmount); 
        this.lastDamageTaken = Math.min(damageAmount, this.currentHealth);
        boolean probablyInstantKill = (this.currentHealth == 0);
        if (damageAmount > 0) {
            if (info.owner != this)
                useStaggerAnimation(); 
            if (damageAmount >= 99 && !CardCrawlGame.overkill)
                CardCrawlGame.overkill = true; 
            this.currentHealth -= damageAmount;
            if (!probablyInstantKill)
                AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, damageAmount)); 
            if (this.currentHealth < 0)
                this.currentHealth = 0; 
            healthBarUpdatedEvent();
        } else if (!probablyInstantKill) {
            if (weakenedToZero && this.currentBlock == 0) {
                if (hadBlock) {
                    AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
                } else {
                    AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, 0));
                } 
            } else if (Settings.SHOW_DMG_BLOCK) {
                AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
            } 
        } 
        if (this.currentHealth <= 0) {
            die();
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.cleanCardQueue();
                AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
                AbstractDungeon.effectList.add(new DeckPoofEffect(Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
                AbstractDungeon.overlayMenu.hideCombatPanels();
            } 
            if (this.currentBlock > 0) {
                loseBlock();
                AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
            } 
        }*/
    }

    public void init() {
        //gain start powers and call start move i guess.
    }

    public void render(SpriteBatch sb) {
        if (!this.isDead && !this.escaped) {
            if (this.atlas == null) {
                sb.setColor(this.tint.color);
                if (this.img != null)
                    sb.draw(this.img, this.drawX - this.img
                    
                        .getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, this.img
                        
                        .getWidth() * Settings.scale, this.img
                        .getHeight() * Settings.scale, 0, 0, this.img
                        
                        .getWidth(), this.img
                        .getHeight(), this.flipHorizontal, this.flipVertical); 
            } else {
                this.state.update(Gdx.graphics.getDeltaTime());
                this.state.apply(this.skeleton);
                this.skeleton.updateWorldTransform();
                this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY);
                this.skeleton.setColor(this.tint.color);
                this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
                sb.end();
                CardCrawlGame.psb.begin();
                sr.draw(CardCrawlGame.psb, this.skeleton);
                CardCrawlGame.psb.end();
                sb.begin();
                sb.setBlendFunction(770, 771);
            } 
            //maybe just make an invisible monster for sneaky gremlin
            /*if (this == (AbstractDungeon.getCurrRoom()).monsters.hoveredMonster && this.atlas == null) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
                if (this.img != null) {
                    sb.draw(this.img, this.drawX - this.img
                    
                        .getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, this.img
                        
                        .getWidth() * Settings.scale, this.img
                        .getHeight() * Settings.scale, 0, 0, this.img
                        
                        .getWidth(), this.img
                        .getHeight(), this.flipHorizontal, this.flipVertical);
                    sb.setBlendFunction(770, 771);
                } 
            }*/
        if (!this.isDying && !this.isEscaping && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead)
            if (!AbstractDungeon.player.hasRelic("Runic Dome") && this.intent != Intent.NONE && !Settings.hideCombatElements) {
                renderIntentVfxBehind(sb);
                renderIntent(sb);
                renderIntentVfxAfter(sb);
                renderDamageRange(sb);
            }  
            this.hb.render(sb);
            this.intentHb.render(sb);
            //this.healthHb.render(sb);
        } 
        if (!AbstractDungeon.player.isDead) {
            //renderHealth(sb);
            renderName(sb);
        }
    }

    private void renderDamageRange(SpriteBatch sb) {
        if (this.intent.name().contains("ATTACK"))
            if (this.isMultiDmg) {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, 
                    
                    Integer.toString(this.intentDmg) + "x" + Integer.toString(this.intentMultiAmt), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            } else {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, 
                    
                    Integer.toString(this.intentDmg), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            }  
    }

    private void renderIntentVfxBehind(SpriteBatch sb) {
        for (AbstractGameEffect e : this.intentVfx) {
            if (e.renderBehind)
                e.render(sb); 
        } 
    }
  
    private void renderIntentVfxAfter(SpriteBatch sb) {
        for (AbstractGameEffect e : this.intentVfx) {
            if (!e.renderBehind)
                e.render(sb); 
        } 
    }

    private void renderName(SpriteBatch sb) {
        if (!this.hb.hovered) {
            this.hoverTimer = MathHelper.fadeLerpSnap(this.hoverTimer, 0.0F);
        } else {
            this.hoverTimer += Gdx.graphics.getDeltaTime();
        } 
        if ((!AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.hoveredCard == null || AbstractDungeon.player.hoveredCard.target == AbstractCard.CardTarget.ENEMY) && !this.isDying) {
            if (this.hoverTimer != 0.0F) {
                if (this.hoverTimer * 2.0F > 1.0F) {
                    this.nameColor.a = 1.0F;
                } else {
                    this.nameColor.a = this.hoverTimer * 2.0F;
                } 
            } else {
                this.nameColor.a = MathHelper.slowColorLerpSnap(this.nameColor.a, 0.0F);
            } 
            float tmp = Interpolation.exp5Out.apply(1.5F, 2.0F, this.hoverTimer);
            this.nameColor.r = Interpolation.fade.apply(Color.DARK_GRAY.r, Settings.CREAM_COLOR.r, this.hoverTimer * 10.0F);
            this.nameColor.g = Interpolation.fade.apply(Color.DARK_GRAY.g, Settings.CREAM_COLOR.g, this.hoverTimer * 3.0F);
            this.nameColor.b = Interpolation.fade.apply(Color.DARK_GRAY.b, Settings.CREAM_COLOR.b, this.hoverTimer * 3.0F);
            float y = Interpolation.exp10Out.apply(this.healthHb.cY, this.healthHb.cY - 8.0F * Settings.scale, this.nameColor.a);
            float x = this.hb.cX - this.animX;
            this.nameBgColor.a = this.nameColor.a / 2.0F * this.hbAlpha;
            sb.setColor(this.nameBgColor);
            TextureAtlas.AtlasRegion img = ImageMaster.MOVE_NAME_BG;
            sb.draw((TextureRegion)img, x - img.packedWidth / 2.0F, y - img.packedHeight / 2.0F, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, Settings.scale * tmp, Settings.scale * 2.0F, 0.0F);
            this.nameColor.a *= this.hbAlpha;
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, this.name, x, y, this.nameColor);
        } 
    }
  
    private void renderIntent(SpriteBatch sb) {
        this.intentColor.a = this.intentAlpha;
        sb.setColor(this.intentColor);
        if (this.intentBg != null) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.intentAlpha / 2.0F));
            if (Settings.isMobile) {
                sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, 0.0F, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
            } 
        } 
        if (this.intentImg != null && this.intent != Intent.UNKNOWN && this.intent != Intent.STUN) {
            if (this.intent == Intent.DEBUFF || this.intent == Intent.STRONG_DEBUFF) {
                this.intentAngle += Gdx.graphics.getDeltaTime() * 150.0F;
            } else {
                this.intentAngle = 0.0F;
            } 
            sb.setColor(this.intentColor);
            if (Settings.isMobile) {
                sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, this.intentAngle, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, this.intentAngle, 0, 0, 128, 128, false, false);
            } 
        } 
    }
 //MAYBE THESE TOO?
    protected void updateHitbox(float hb_x, float hb_y, float hb_w, float hb_h) {
        this.hb_w = hb_w * Settings.scale;
        this.hb_h = hb_h * Settings.xScale;
        this.hb_y = hb_y * Settings.scale;
        this.hb_x = hb_x * Settings.scale;
        this.hb = new Hitbox(this.hb_w, this.hb_h);
        this.hb.move(this.drawX + this.hb_x + this.animX, this.drawY + this.hb_y + this.hb_h / 2.0F);
        this.healthHb.move(this.hb.cX, this.hb.cY - this.hb_h / 2.0F - this.healthHb.height / 2.0F);
        this.intentHb.move(this.hb.cX + this.intentOffsetX, this.hb.cY + this.hb_h / 2.0F + 32.0F * Settings.scale);
    }

    private void updateDeathAnimation() {
        if (this.isDying) {
            this.deathTimer -= Gdx.graphics.getDeltaTime();
            if (this.deathTimer < 1.8F && !this.tintFadeOutCalled) {
                this.tintFadeOutCalled = true;
                this.tint.fadeOut();
            } 
        } 
        if (this.deathTimer < 0.0F) {
            this.isDead = true;
            if (AbstractDungeon.getMonsters().areMonstersDead() && !(AbstractDungeon.getCurrRoom()).isBattleOver && 
                !(AbstractDungeon.getCurrRoom()).cannotLose)
                AbstractDungeon.getCurrRoom().endBattle(); 
            dispose();
            this.powers.clear();
        } 
    }
  
    public void dispose() {
        if (this.img != null) {
            logger.info("Disposed monster img asset");
            this.img.dispose();
            this.img = null;
        } 
        for (Disposable d : this.disposables) {
            logger.info("Disposed extra monster assets");
            d.dispose();
        } 
        if (this.atlas != null) {
            this.atlas.dispose();
            this.atlas = null;
            logger.info("Disposed Texture: " + this.name);
        } 
    }
  
    private void updateEscapeAnimation() {
        if (this.escapeTimer != 0.0F) {
            this.flipHorizontal = true;
            this.escapeTimer -= Gdx.graphics.getDeltaTime();
            this.drawX += Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
        } 
        if (this.escapeTimer < 0.0F) {
            this.escaped = true;
        if (AbstractDungeon.getMonsters().areMonstersDead() && !(AbstractDungeon.getCurrRoom()).isBattleOver && 
            !(AbstractDungeon.getCurrRoom()).cannotLose)
            AbstractDungeon.getCurrRoom().endBattle(); 
        } 
    }
  
    public void escapeNext() {
        this.escapeNext = true;
    }
  
    public void deathReact() {}
  
    public void escape() {
        hideHealthBar();
        this.isEscaping = true;
        this.escapeTimer = 3.0F;
    }
 //up until here

    public void usePreBattleAction() {}
  
    /*public void useUniversalPreBattleAction() {
        if (ModHelper.isModEnabled("Lethality"))
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(this, this, (AbstractPower)new StrengthPower(this, 3), 3)); 
        for (AbstractBlight b : AbstractDungeon.player.blights)
            b.onCreateEnemy(this); 
        if (ModHelper.isModEnabled("Time Dilation") && !this.id.equals("GiantHead"))
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(this, this, (AbstractPower)new SlowPower(this, 0))); 
    }*/

    private void calculateDamage(int dmg) {
        AbstractPlayer target = AbstractDungeon.player;
        float tmp = dmg;
        if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
            float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
            tmp *= mod;
        } 
        for (AbstractPower p : this.powers)
            tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL); 
        for (AbstractPower p : target.powers)
            tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL); 
        tmp = AbstractDungeon.player.stance.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
        if (applyBackAttack())
            tmp = (int)(tmp * 1.5F); 
        for (AbstractPower p : this.powers)
            tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL); 
        for (AbstractPower p : target.powers)
            tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL); 
        dmg = MathUtils.floor(tmp);
        if (dmg < 0)
            dmg = 0; 
        this.intentDmg = dmg;
    }

    public void applyPowers() {
        boolean applyBackAttack = applyBackAttack();
        if (applyBackAttack && !hasPower("BackAttack"))
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ApplyPowerAction(this, null, (AbstractPower)new BackAttackPower(this))); 
        for (DamageInfo dmg : this.damage) {
            dmg.applyPowers(this, (AbstractCreature)AbstractDungeon.player);
            if (applyBackAttack)
                dmg.output = (int)(dmg.output * 1.5F); 
        } 
        if (this.move.baseDamage > -1)
            calculateDamage(this.move.baseDamage); 
        this.intentImg = getIntentImg();
        updateIntentTip();
    }
  
    private boolean applyBackAttack() {
        if (AbstractDungeon.player.hasPower("Surrounded") && ((
            AbstractDungeon.player.flipHorizontal && AbstractDungeon.player.drawX < this.drawX) || (!AbstractDungeon.player.flipHorizontal && AbstractDungeon.player.drawX > this.drawX)))
            return true; 
        return false;
    }

    public void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }
  
    public void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    /*public HashMap<String, Serializable> getLocStrings() {
        HashMap<String, Serializable> data = new HashMap<>();
        data.put("name", this.name);
        data.put("moves", MOVES);
        data.put("dialogs", DIALOG);
        return data;
    }*/
  
    public int getIntentDmg() {
        return this.intentDmg;
    }
    
    public int getIntentBaseDmg() {
        return this.intentBaseDmg;
    }
    
    public void setIntentBaseDmg(int amount) {
        this.intentBaseDmg = amount;
    }
    
    public abstract void takeTurn();
    
    protected abstract void getMove(int paramInt);
}
