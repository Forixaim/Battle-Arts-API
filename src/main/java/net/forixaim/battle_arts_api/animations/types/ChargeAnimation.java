package net.forixaim.battle_arts_api.animations.types;

import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.model.Armature;

public class ChargeAnimation extends ActionAnimation
{
    public ChargeAnimation(float transitionTime, AnimationManager.AnimationAccessor<? extends ActionAnimation> accessor, AssetAccessor<? extends Armature> armature)
    {
        super(transitionTime, accessor, armature);
    }

    public ChargeAnimation(float transitionTime, float postDelay, AnimationManager.AnimationAccessor<? extends ActionAnimation> accessor, AssetAccessor<? extends Armature> armature)
    {
        super(transitionTime, postDelay, accessor, armature);
    }

    public ChargeAnimation(float transitionTime, float postDelay, String path, AssetAccessor<? extends Armature> armature)
    {
        super(transitionTime, postDelay, path, armature);
    }

    @Override
    public boolean isRepeat()
    {
        return true;
    }
}
