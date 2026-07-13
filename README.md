# AxShulker Compat

Client-side compatibility mod scaffold for stack comparison normalization.

Targets:
- Minecraft 26.1.x
- Item Scroller
- Inventory Profiles Next
- Tweakeroo hand restock
- validated against Item Scroller 26.1.x and 26.2 hooks

Current status:
- ported to Minecraft 26.1.2 as the build baseline
- metadata allows Minecraft 26.1.x
- config file scaffold created
- normalization core scaffold created
- ItemScroller and IPN integration hooks created
- Tweakeroo hand restock hook created

Next step:
- smoke-test with ItemScroller/IPN/Tweakeroo on 26.1.x
- revisit 26.2 with a Gradle 9.5+ wrapper and Loom 1.17+
