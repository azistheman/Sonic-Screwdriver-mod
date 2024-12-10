package net.aj.sonicscrewdriver.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SonicScrewdriverItem extends Item {

    public SonicScrewdriverItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        if (!world.isClient) { // Server-side only
            BlockState blockState = world.getBlockState(pos);

            // Handle pistons
            if (blockState.isOf(Blocks.PISTON) || blockState.isOf(Blocks.STICKY_PISTON)) {
                togglePiston(world, pos, blockState); // Toggle piston state
                return ActionResult.SUCCESS;
            }

            // Handle redstone dust
            if (blockState.getBlock() instanceof RedstoneWireBlock) {
                setRedstoneSignal(world, pos, 15); // Set redstone signal along the line
                return ActionResult.SUCCESS;
            }

            // Handle iron doors
            if (blockState.isOf(Blocks.IRON_DOOR)) {
                toggleIronDoor(world, pos, blockState);
                return ActionResult.SUCCESS;
            }

            // Handle blocks with POWERED property (e.g., doors, trapdoors)
            if (blockState.contains(Properties.POWERED)) {
                boolean isPowered = blockState.get(Properties.POWERED);
                world.setBlockState(pos, blockState.with(Properties.POWERED, !isPowered)); // Toggle power
                playActivationSound(world, pos);
                return ActionResult.SUCCESS;
            }

            // Handle redstone lamps
            if (blockState.isOf(Blocks.REDSTONE_LAMP)) {
                world.setBlockState(pos, blockState.cycle(Properties.LIT)); // Toggle the lamp state
                playActivationSound(world, pos);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    private void togglePiston(World world, BlockPos pos, BlockState state) {
        if (state.contains(Properties.EXTENDED)) {
            boolean isExtended = state.get(Properties.EXTENDED);
            world.setBlockState(pos, state.with(Properties.EXTENDED, !isExtended)); // Toggle the extended state
            world.updateNeighbors(pos, state.getBlock()); // Notify neighbors of the state change
            playActivationSound(world, pos);
        }
    }

    private void setRedstoneSignal(World world, BlockPos startPos, int power) {
        BlockPos currentPos = startPos;

        // Traverse and set power along the redstone line
        while (true) {
            BlockState state = world.getBlockState(currentPos);
            if (state.getBlock() instanceof RedstoneWireBlock) {
                world.setBlockState(currentPos, state.with(RedstoneWireBlock.POWER, power));
                updateNeighbors(world, currentPos);
            } else {
                break; // Stop if we hit a non-redstone wire block
            }

            // Adjust direction as needed (e.g., NORTH, EAST, etc.)
            currentPos = currentPos.offset(Direction.NORTH); // You can add logic to handle all directions
        }
    }

    private void toggleIronDoor(World world, BlockPos pos, BlockState state) {
        if (state.contains(Properties.OPEN)) {
            boolean isOpen = state.get(Properties.OPEN);
            world.setBlockState(pos, state.with(Properties.OPEN, !isOpen)); // Toggle the open state
            playActivationSound(world, pos);
        }
    }

    private void updateNeighbors(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            world.updateNeighbor(neighborPos, neighborState.getBlock(), pos);
        }
    }

    private void playActivationSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
