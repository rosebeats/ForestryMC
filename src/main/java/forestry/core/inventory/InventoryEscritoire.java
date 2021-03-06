/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IIndividual;
import forestry.core.tiles.EscritoireGame;
import forestry.core.tiles.TileEscritoire;
import forestry.core.utils.GeneticsUtil;
import forestry.core.utils.SlotUtil;

public class InventoryEscritoire extends InventoryAdapterTile<TileEscritoire> {
	public static final short SLOT_ANALYZE = 0;
	public static final short SLOT_RESULTS_1 = 1;
	public static final short SLOTS_RESULTS_COUNT = 6;
	public static final short SLOT_INPUT_1 = 7;
	public static final short SLOTS_INPUT_COUNT = 5;

	public InventoryEscritoire(TileEscritoire escritoire) {
		super(escritoire, 12, "Items");
	}

	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack itemStack) {
		if (slotIndex >= SLOT_INPUT_1 && slotIndex < SLOT_INPUT_1 + tile.getGame().getSampleSize(SLOTS_INPUT_COUNT)) {
			ItemStack specimen = getStackInSlot(SLOT_ANALYZE);
			if (specimen.isEmpty()) {
				return false;
			}
			IIndividual individual = AlleleManager.alleleRegistry.getIndividual(specimen);
			return individual != null && individual.getGenome().getPrimary().getResearchSuitability(itemStack) > 0;
		}

		return slotIndex == SLOT_ANALYZE &&
				(AlleleManager.alleleRegistry.isIndividual(itemStack) || GeneticsUtil.getGeneticEquivalent(itemStack) != null);

	}

	@Override
	public boolean isLocked(int slotIndex) {
		if (slotIndex == SLOT_ANALYZE) {
			return false;
		}

		if (getStackInSlot(SLOT_ANALYZE).isEmpty()) {
			return true;
		}

		if (SlotUtil.isSlotInRange(slotIndex, SLOT_INPUT_1, SLOTS_INPUT_COUNT)) {
			return slotIndex >= SLOT_INPUT_1 + tile.getGame().getSampleSize(SLOTS_INPUT_COUNT);
		}

		return false;
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemstack, EnumFacing side) {
		return SlotUtil.isSlotInRange(slotIndex, SLOT_RESULTS_1, SLOTS_RESULTS_COUNT);
	}

	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
		super.setInventorySlotContents(slotIndex, itemstack);
		if (slotIndex == SLOT_ANALYZE) {
			if (!AlleleManager.alleleRegistry.isIndividual(getStackInSlot(SLOT_ANALYZE)) && !getStackInSlot(SLOT_ANALYZE).isEmpty()) {
				ItemStack ersatz = GeneticsUtil.convertToGeneticEquivalent(getStackInSlot(SLOT_ANALYZE));
				if (AlleleManager.alleleRegistry.isIndividual(ersatz)) {
					super.setInventorySlotContents(SLOT_ANALYZE, ersatz);
				}
			}
			World world = tile.getWorld();
			if (world != null && !world.isRemote) {
				EscritoireGame game = tile.getGame();
				game.initialize(getStackInSlot(SLOT_ANALYZE));
			}
		}
	}
}
