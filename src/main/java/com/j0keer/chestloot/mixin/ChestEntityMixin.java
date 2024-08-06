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

import com.j0keer.chestloot.type.ChestData;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class ChestEntityMixin implements ChestData {
    @Unique
    private boolean used = false;

    @Override
    public boolean isUsed() {
        return used;
    }

    @Override
    public void setUsed(boolean used) {
        this.used = used;
    }

    // Save and load the used flag
    @Inject(method="readNbt", at=@At("HEAD"))
    public void readNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("used")) {
            used = nbt.getBoolean("used");
        }
    }

    // Save and load the used flag
    @Inject(method="writeNbt", at=@At("HEAD"))
    public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("used", used);
    }
}
