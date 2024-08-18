package net.forixaim.bs_api.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.forixaim.bs_api.capabilities.ProficiencyCapabilityProvider;
import net.forixaim.bs_api.proficiencies.Proficiency;
import net.forixaim.bs_api.proficiencies.ProficiencyContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public class ProficiencyCommand
{
	private static final SimpleCommandExceptionType ERROR_NO_PROFICIENCY = new SimpleCommandExceptionType(Component.translatable("command.bs_api.no_proficiency"));

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher )
	{
		RequiredArgumentBuilder<CommandSourceStack, EntitySelector> checkProficiencies = Commands.argument("target", EntityArgument.player())
				.executes(commandContext -> checkProficiency(commandContext.getSource(), EntityArgument.getPlayer(commandContext, "target")));


		LiteralArgumentBuilder<CommandSourceStack> builder =
				Commands.literal("proficiency").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
						.then(Commands.literal("check").then(checkProficiencies));
		dispatcher.register(Commands.literal("battle_arts").then(builder));
	}


	public static int checkProficiency(CommandSourceStack source, ServerPlayer player) throws CommandSyntaxException
	{
		player.getCapability(ProficiencyCapabilityProvider.PROFICIENCY_CAPABILITY).ifPresent(proficiencyCapability ->
				{
					if (proficiencyCapability.getProficiencies().isEmpty())
					{
						source.sendSuccess(() -> Component.translatable("command.bs_api.no_proficiency"), true);
					}
					else
					{
						StringBuilder test = new StringBuilder();
						for (ProficiencyContainer proficiency : proficiencyCapability.getProficiencies())
						{
							test.append(proficiency.getContainingProficiency().getIdentifier().toString())
									.append("\n")
									.append(proficiency.getCurrentRank().getDebugIdentifier())
									.append("\n")
									.append(proficiency.getCurrentXp())
									.append("\n")
									.append(proficiency.getLevelThreshold())
									.append("\n\n");
						}
						source.sendSuccess(() -> Component.literal(test.toString()), true);
					}
				}
		);
		return 1;
	}

	private static <T> Supplier<T> wrap(T value) {
		return () -> value;
	}

}
