package com.odtheking.odin.features.impl.skyblock

import com.odtheking.odin.clickgui.settings.impl.BooleanSetting
import com.odtheking.odin.events.core.on
import com.odtheking.odin.events.ChatPacketEvent
import com.odtheking.odin.features.Module
import com.odtheking.odin.utils.modMessage
import com.odtheking.odin.utils.sendCommand

object AutoSprint : Module(name = "Auto Sprint", description = "Automatically makes you sprint.") {
    private val bosswarp by BooleanSetting("boss warp", false, desc = "")
    private val healer by BooleanSetting("healer", false, desc = "")
    private val pipe by BooleanSetting("pipe", false, desc = "")
    
    private val witherking = setOf(
        "[BOSS] Wither King: My soul is disposable.",
        "[BOSS] Wither King: Futile.",
        "[BOSS] Wither King: Mortals fighting against supreme beings,",
        "[BOSS] Wither King: I am not impressed.",
        "[BOSS] Wither King: I have worked on this spell for centuries.",
        "[BOSS] Wither King: Oh, this one hurts!",
        "[BOSS] Wither King: I have more of those.",
    )
    private val restart = Regex("^\\[Important] This server will restart soon: \\w+ \\w+$")
    private val maxor = "[BOSS] Maxor: WELL! WELL! WELL! LOOK WHO'S HERE!"
    private val goldor = "[BOSS] Goldor: Who dares trespass into my domain?"
    private var pee = 4
    
    init {
        on<ChatPacketEvent> {
            if (pipe && restart.matches(value)) sendCommand("pc Server Reboot ALERT")
            if (bosswarp && (maxor == value || goldor == value)) sendCommand("p warp")
            if (value == "[NPC] Mort: Good luck.") {
                pee = 4
                if (healer) modMessage("§5healer warp is activated", "§a[Dingus] ")
            }
            if (value.startsWith("[BOSS] Wither King:") && witherking.contains(value)) {
                pee--
                modMessage("§5Warping in §4$pee §5more dragons", "§a[Dingus] ")
                if (healer && pee == 0) sendCommand("p warp")
            }
        }
    }
}
