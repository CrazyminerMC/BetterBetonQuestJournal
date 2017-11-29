package it.crazyminer.trickerytown.bbq;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TrickeryTownDevTeam
 */
public class BetterBQ extends JavaPlugin {


    @Override
    public void onEnable() {
        getLogger().info("Better BetonQuest Journal Enabled");
    }

    public static BookMeta getBookMeta(BookMeta meta, List<String> text) {
        List<IChatBaseComponent> pages;
        try {
            //Get the pages from BookMeta
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(meta);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return meta; //return the unmodified meta
        }

        List<String> mainPage = new ArrayList<>();
        List<String> otherPage = new ArrayList<>();
        boolean space = false;
        for (String str : text) {
            if (str.equals("+-+-+-+-+-+-+-+-+")) { //check if str is the separator between mainPage and otherPage assigned by the BetonQuest original plugin
                space = true;
            }
            if (!space) {
                mainPage.add(str);
            } else {
                otherPage.add(str);
            }
        }

        TextComponent page = new TextComponent("");
        for (TextComponent com : parse(mainPage)) {
            page.addExtra(com); //add the transformed text to an unique TextComponent
        }
        pages.add(IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(page)));

        otherPage.remove("+-+-+-+-+-+-+-+-+");//remove the separator between mainPage and otherPage
        for (String str : otherPage) {
            pages.add(IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(new TextComponent(str))));
        }


        return meta;
    }

    private static List<TextComponent> parse(List<String> rows) {
        List<TextComponent> pagesRow = new ArrayList<>();

        //Split the string at "°" and transform it in a TextComponent with hover
        for (String row : rows) {
            if (row.contains("°")) {
                String[] hoverText = row.split("°");
                TextComponent textElaborated = new TextComponent(hoverText[0] + "\n");
                textElaborated.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText[1]).create()));
                pagesRow.add(textElaborated);
            } else {
                pagesRow.add(new TextComponent(row + "\n"));
            }
        }

        return pagesRow;
    }
}