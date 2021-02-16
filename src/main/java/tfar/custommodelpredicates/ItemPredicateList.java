package tfar.custommodelpredicates;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class ItemPredicateList {


    public static Predicate<ItemStack> createNamePredicate(String name) {
        return stack -> {
            String name1 = stack.getDisplayName().getUnformattedComponentText();
            return name1.equals(name);
        };
    }

    public static Predicate<ItemStack> createCountPredicate(String id) {
        if (id.contains(">")) {
            int count = Integer.parseInt(id.substring(1));

            return stack -> stack.getCount() > count;
        } else if (id.contains("<")) {
            int count = Integer.parseInt(id.substring(1));

            return stack -> stack.getCount() < count;
        } else {
            int count = Integer.parseInt(id);

            return stack -> stack.getCount() == count;
        }
    }

    public static Predicate<ItemStack> createNBTPredicate(String name, String type, String require) {
        switch (type) {

            case "byte": {
                String constraint = require.substring(0, 1);
                byte number = Byte.parseByte(require.substring(1));
                if ("=".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getByte(name) == number;
                }
                if (">".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getByte(name) < number;
                }
                if ("<".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getByte(name) > number;
                }
                break;
            }

            case "short": {
                String constraint = require.substring(0, 1);
                short number = Short.parseShort(require.substring(1));
                if ("=".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getShort(name) == number;
                }
                if (">".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getShort(name) < number;
                }
                if ("<".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getShort(name) > number;
                }
                break;
            }

            case "int": {
                String constraint = require.substring(0, 1);
                int number = Integer.parseInt(require.substring(1));
                if ("=".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getInt(name) == number;
                }
                if (">".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getInt(name) < number;
                }
                if ("<".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getInt(name) > number;
                }
                break;
            }

            case "long": {
                String constraint = require.substring(0, 1);
                long number = Long.parseLong(require.substring(1));
                if ("=".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getLong(name) == number;
                }
                if (">".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getLong(name) < number;
                }
                if ("<".equals(constraint)) {
                    return stack -> stack.hasTag() && stack.getTag().getLong(name) > number;
                }
                break;
            }

            case "float": {
                String constraint = require.substring(0, 1);
                float number = Float.parseFloat(require.substring(1));
                if ("=".equals(constraint)) {
                    return stack -> stack.hasTag() && Float.compare(stack.getTag().getFloat(name), number) == 0;
                }
                if (">".equals(constraint)) {
                    return stack -> stack.hasTag() && Float.compare(stack.getTag().getFloat(name), number) == 1;
                }
                if ("<".equals(constraint)) {
                    return stack -> stack.hasTag() && Float.compare(stack.getTag().getFloat(name), number) == -1;
                }
                break;
            }

            case "double": {
                String constraint = require.substring(0, 1);
                double number = Double.parseDouble(require.substring(1));
                if ("=".equals(constraint)) {
                    return stack -> stack.hasTag() && Double.compare(stack.getTag().getDouble(name), number) == 0;
                }
                if (">".equals(constraint)) {
                    return stack -> stack.hasTag() && Double.compare(stack.getTag().getDouble(name), number) == 1;
                }
                if ("<".equals(constraint)) {
                    return stack -> stack.hasTag() && Double.compare(stack.getTag().getDouble(name), number) == -1;
                }
                break;
            }

            case "boolean": {
                boolean constraint = Boolean.parseBoolean(require);
                if (constraint) {
                    return stack -> stack.hasTag() && stack.getTag().getBoolean(name);
                }
                else {
                    return stack -> stack.hasTag() && !stack.getTag().getBoolean(name);
                }
            }

            case "string":
                return stack -> stack.hasTag() && stack.getTag().getString(name).equals(require);
        }
        //other types?
        throw new IllegalArgumentException("Unsupported NBT type: "+type);
    }
}
