package tw.momocraft.lotteryplus.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

import java.util.*;

public class Utils {

    public static boolean containsIgnoreCase(String string1, String string2) {
        if (string1 != null && string2 != null && string1.toLowerCase().contains(string2.toLowerCase())) {
            return true;
        }
        return false;
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }

    public static Integer returnInteger(String text) {
        if (text == null) {
            return null;
        } else {
            char[] characters = text.toCharArray();
            Integer value = null;
            boolean isPrevDigit = false;
            for (int i = 0; i < characters.length; i++) {
                if (isPrevDigit == false) {
                    if (Character.isDigit(characters[i])) {
                        isPrevDigit = true;
                        value = Character.getNumericValue(characters[i]);
                    }
                } else {
                    if (Character.isDigit(characters[i])) {
                        value = (value * 10) + Character.getNumericValue(characters[i]);
                    } else {
                        break;
                    }
                }
            }
            return value;
        }
    }

    private static String getNearbyPlayer(Player player, int range) {
        try {
            ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
            ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight((Set<Material>) null, range);
            ArrayList<Location> sight = new ArrayList<Location>();
            for (int i = 0; i < sightBlock.size(); i++)
                sight.add(sightBlock.get(i).getLocation());
            for (int i = 0; i < sight.size(); i++) {
                for (int k = 0; k < entities.size(); k++) {
                    if (Math.abs(entities.get(k).getLocation().getX() - sight.get(i).getX()) < 1.3) {
                        if (Math.abs(entities.get(k).getLocation().getY() - sight.get(i).getY()) < 1.5) {
                            if (Math.abs(entities.get(k).getLocation().getZ() - sight.get(i).getZ()) < 1.3) {
                                if (entities.get(k) instanceof Player) {
                                    return entities.get(k).getName();
                                }
                            }
                        }
                    }
                }
            }
            return "INVALID";
        } catch (NullPointerException e) {
            return "INVALID";
        }
    }

    public static String translateLayout(String input, Player player) {
        String playerName = "EXEMPT";
        if (player != null) {
            playerName = player.getName();
        }
        if (player != null && !(player instanceof ConsoleCommandSender)) {
            try {
                input = input.replace("%player%", playerName);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                input = input.replace("%mob_kills%", String.valueOf(player.getStatistic(Statistic.MOB_KILLS)));
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                input = input.replace("%player_kills%", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)));
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                input = input.replace("%player_deaths%", String.valueOf(player.getStatistic(Statistic.DEATHS)));
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                input = input.replace("%player_food%", String.valueOf(player.getFoodLevel()));
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                input = input.replace("%player_health%", String.valueOf(player.getHealth()));
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                input = input.replace("%player_location%", player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "");
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                input = input.replace("%player_interact%", getNearbyPlayer(player, 3));
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                // %random_number%500%
                if (input.contains("%random_number%")) {
                    String[] arr = input.split("%");
                    List<Integer> list = new ArrayList<>();
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].equals("random_number")) {
                            list.add(Integer.parseInt(arr[i + 1]));
                        }
                    }
                    for (int max : list) {
                        input = input.replace("%random_number%" + max + "%", String.valueOf(new Random().nextInt(max)));
                    }
                }
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                // %random_player%
                try {
                    List<Player> playerList = new ArrayList(Bukkit.getOnlinePlayers());
                    String randomPlayer = playerList.get(new Random().nextInt(playerList.size())).getName();
                    input = input.replace("%random_player%", randomPlayer);
                } catch (Exception e) {
                    ServerHandler.sendDebugTrace(e);
                }
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        } else {
            try {
                input = input.replace("%player%", "CONSOLE");
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        input = ChatColor.translateAlternateColorCodes('&', input);
        if (ConfigHandler.getDepends().PlaceHolderAPIEnabled()) {
            try {
                try {
                    String s = PlaceholderAPI.setPlaceholders(player, input);
                    return s;
                } catch (NoSuchFieldError e) {
                    ServerHandler.sendDebugMessage("Error has occured when setting the PlaceHolder " + e.getMessage() + ", if this issue persits contact the developer of PlaceholderAPI.");
                    return input;
                }
            } catch (Exception e) {
            }
        }
        return input;
    }

    /**
     * Sort Map keys by values.
     * High -> Low
     *
     * @param map the input map.
     * @param <K>
     * @param <V>
     * @return the sorted map.
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Sort Map keys by values.
     * Low -> High
     *
     * @param map the input map.
     * @param <K>
     * @param <V>
     * @return the sorted map.
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueLow(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String translateColorCode(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);

    }
}