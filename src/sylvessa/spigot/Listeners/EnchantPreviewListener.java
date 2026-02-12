package sylvessa.spigot.Listeners;

// todo: replace with InventoryClickEvent

public class EnchantPreviewListener { // extends playerConnection
//    private final Player player;
//    private final EntityPlayer eplayer;
//    private Random random = new Random();
//
//    public EnchantPreviewListener(playerConnection old, EntityPlayer ep, Player p) {
//        super(ep.b, old.networkManager, ep);
//
//        this.player = p;
//        this.eplayer = ep;
//
//    }
//
//    private String enchantName(int id) {
//        if (id == 0) return "protection";
//        if (id == 1) return "fire protection";
//        if (id == 2) return "feather falling";
//        if (id == 3) return "blast protection";
//        if (id == 4) return "projectile protection";
//        if (id == 5) return "respiration";
//        if (id == 6) return "aqua affinity";
//        if (id == 16) return "sharpness";
//        if (id == 17) return "smite";
//        if (id == 18) return "bane of arthropods";
//        if (id == 19) return "knockback";
//        if (id == 20) return "fire aspect";
//        if (id == 21) return "looting";
//        if (id == 32) return "efficiency";
//        if (id == 33) return "silk touch";
//        if (id == 34) return "unbreaking";
//        if (id == 35) return "fortune";
//        if (id == 48) return "power";
//        if (id == 49) return "punch";
//        if (id == 50) return "flame";
//        if (id == 51) return "infinity";
//        return "unknown";
//    }
//
//    private void showLevel(ItemStack original, int level, String prefix) {
//        ItemStack copy = original.cloneItemStack();
//        List list = EnchantmentManager.a(random, copy, level);
//
//        if (list == null || list.isEmpty()) {
//            player.sendMessage(prefix + " §7none");
//            return;
//        }
//
//        String line = prefix + " §f";
//        boolean first = true;
//
//        for (Object o : list) {
//            EnchantmentInstance ei = (EnchantmentInstance)o;
//            if (!first) line += ", ";
//            first = false;
//            line += enchantName(ei.a.id) + " " + ei.b;
//        }
//
//        player.sendMessage(line);
//    }
//
//
//    @Override
//    public void a(Packet102WindowClick packet) {
//        Container container = eplayer.activeContainer;
//        if (!(container instanceof ContainerEnchantTable)) {
//            super.a(packet);
//            return;
//        }
//
//        ContainerEnchantTable table = (ContainerEnchantTable)container;
//
//        if (packet.b != 0) {
//            super.a(packet);
//            return;
//        }
//
//        super.a(packet);
//
//        Slot slot = (Slot)container.e.get(0);
//        if (slot == null || !slot.c()) {
//            return;
//        }
//
//        ItemStack original = slot.getItem();
//        if (original == null) {
//            return;
//        }
//
//        random = new Random();
//
//        Log.info("0: " + table.c[0]);
//        Log.info("1: " + table.c[1]);
//        Log.info("2: " + table.c[2]);
//
//        player.sendMessage("§aenchant preview:");
//        showLevel(original, table.c[0], "§71:");
//        showLevel(original, table.c[1], "§72:");
//        showLevel(original, table.c[2], "§73:");
//    }


}
