package drgMB;

import java.util.UUID;

public class MiniFigure {
	public enum FigureType {
		GLYPHID_GRUNT,
		GLYPHID_SLASHER,
		GLYPHID_WEB_SPITTER,
		GLYPHID_PRAETORIAN,
		GLYPHID_OPPRESSOR,
		GLYPHID_MENACE,
		MACTERA_SPAWN,
		MACTERA_GOO_BOMBER,
		BROOD_NEXUS,
		SPITBALLER,
		STALAGMITE
	}
	
	private UUID figureUUID;
	private FigureType figureType;
	
	public MiniFigure(FigureType figureType) {
		this.figureUUID = UUID.randomUUID();
		this.figureType = figureType;
	}
	
	public FigureType getFigureType() {
		return this.figureType;
	}
	
	public UUID getUUID() {
		return this.figureUUID;
	}
}
