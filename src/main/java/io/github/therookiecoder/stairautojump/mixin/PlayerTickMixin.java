package io.github.therookiecoder.stairautojump.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerTickMixin {
    private static boolean onStair(BlockPos pos, World world) {
        BlockState block = world.getBlockState(pos);
        return block != null
            && block.getBlock() instanceof StairsBlock
            && block.get(StairsBlock.HALF) == BlockHalf.BOTTOM;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void injectMethod(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getWorld();
        BlockPos pos = player.getBlockPos();
        GameOptions options = MinecraftClient.getInstance().options;

        options.getAutoJump().setValue(
            !player.hasStatusEffect(StatusEffects.JUMP_BOOST)
                && (onStair(pos, world) || onStair(pos.down(), world))
        );
    }
}
