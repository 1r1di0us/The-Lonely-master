package lonelymod.companions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlashCompanionIntentEffect extends AbstractGameEffect {
        private static final float DURATION = 1.0F;
    
        private static final float FLASH_INTERVAL = 0.17F;
        
        private float intervalTimer = 0.0F;
        
        private Texture img;
        
        private AbstractCompanion c;
    
        public FlashCompanionIntentEffect(Texture img, AbstractCompanion c) {
            this.duration = 1.0F;
            this.img = img;
            this.c = c;
        }
    
    public void update() {
        this.intervalTimer -= Gdx.graphics.getDeltaTime();
        if (this.intervalTimer < 0.0F && 
            !this.c.isDying) {
            this.intervalTimer = 0.17F;
            AbstractDungeon.effectsQueue.add(new FlashCompanionIntentParticle(this.img, this.c));
        } 
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
            this.isDone = true; 
    }
    
    public void render(SpriteBatch sb) {}
    
    public void dispose() {}
}
