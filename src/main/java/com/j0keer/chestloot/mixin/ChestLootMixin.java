/**
 *  ____  _   _  ____  _  ______    _        ____  _____  ____  _____ ____
 * / ___|| | | |/ ___|| |/ / ___|  / \      / ___|| ____|/ ___|| ____|  _ \
 * \___ \| |_| | |    | ' /\___ \ / _ \     \___ \|  _|  \___ \|  _| | |_) |
 *  ___) |  _  | |___ | . \ ___) / ___ \     ___) | |___  ___) | |___|  _ <
 * |____/|_| |_|\____||_|\_\____/_/   \_\   |____/|_____|____/|_____|_| \_\
 *
 * Credit: TheJokerDev / J0keer / Tony
 */
package com.j0keer.chestloot.mixin;

import com.j0keer.chestloot.ChestLoot;
import com.j0keer.chestloot.type.ChestData;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestLootMixin {

    @Inject(method = "onPlaced", at = @At("RETURN"))
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ChestBlockEntity) {
            ChestData data = ((ChestData) blockEntity); // Get the chest data containing the used flag
            if (placer instanceof PlayerEntity player) {
                data.setUsed(!player.isCreative()); // Set the used flag to true if the player is not in creative mode
            }
        }
    }

    @Inject(method = "onUse", at = @At("HEAD"))
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        BlockEntity blockEntity = world.getBlockEntity(pos); // Get the block entity at the position
        if (blockEntity instanceof ChestBlockEntity chest) {
            ChestLoot.getInstance().getChestUtils().applyLootTable(pos, chest); // Apply the loot table to the chest
        }
    }
}
