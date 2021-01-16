package io.github.ender.towersofthewild.mixins;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocateCommand.class)
public class LocateCommandMixin {

	@Shadow
	private static int execute(ServerCommandSource source, String structure) throws CommandSyntaxException {
		// Dummy method
		return 0;
	}

	@Redirect(
		method = "register",
		at = @At(
				value = "INVOKE",
				target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
				ordinal = 0
		)
	)
	private static <S> ArgumentBuilder suggestModdedStructures (LiteralArgumentBuilder literalArgumentBuilder, ArgumentBuilder<S, ?> argument) {
		// Default functionality.
		literalArgumentBuilder.then(CommandManager.literal("Pillager_Outpost").executes((commandContext) -> {
			return execute(commandContext.getSource(), "Pillager_Outpost");
		}));

		// Modded functionality
		literalArgumentBuilder.then(CommandManager.literal("Tower").executes((commandContext) -> {
			return execute(commandContext.getSource(), "Tower");
		}));

		return literalArgumentBuilder;
	}
}
