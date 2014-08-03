/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.mail.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import forestry.core.gui.ContainerForestry;
import forestry.core.gui.slots.SlotClosed;
import forestry.core.proxy.Proxies;
import forestry.mail.POBox;
import forestry.mail.gadgets.MachineMailbox;
import forestry.plugins.PluginMail;

public class ContainerMailbox extends ContainerForestry {

	MachineMailbox mailbox;
	POBox mailinventory;

	public ContainerMailbox(InventoryPlayer player, MachineMailbox tile) {
		super(tile);

		// Mailbox contents
		this.mailbox = tile;

		IInventory inv = mailbox.getOrCreateMailInventory();
		if (inv instanceof POBox)
			mailinventory = (POBox) inv;

		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 12; j++)
				addSlot(new SlotClosed(inv, j + i * 9, 8 + j * 18, 8 + i * 18));

		// Player inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlot(new Slot(player, j + i * 9 + 9, 35 + j * 18, 145 + i * 18));
		// Player hotbar
		for (int i = 0; i < 9; i++)
			addSlot(new Slot(player, i, 35 + i * 18, 203));

	}

	@Override
	public ItemStack slotClick(int slotIndex, int button, int par3, EntityPlayer player) {
		ItemStack stack = super.slotClick(slotIndex, button, par3, player);

		if (Proxies.common.isSimulating(player.worldObj) && mailinventory != null)
			PluginMail.proxy.setPOBoxInfo(mailbox.getWorldObj(), mailbox.getOwnerProfile(), mailinventory.getPOBoxInfo());

		return stack;
	}
}
