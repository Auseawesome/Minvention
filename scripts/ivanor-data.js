Events.on(ContentInitEvent, e => {
    Vars.content.planet("ivanor").hiddenItems.addAll(Items.erekirItems).removeAll(Items.serpuloItems);
});