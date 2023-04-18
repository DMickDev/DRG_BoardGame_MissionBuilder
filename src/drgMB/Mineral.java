package drgMB;

import java.util.UUID;

public class Mineral {
	public enum MineralType {
		NITRA,
		GOLD,
		MORKITE,
		AQUARQ
	}
	
	private UUID mineralUUID;
	private MineralType mineralType;
	
	public Mineral(MineralType mineralType) {
		this.mineralUUID = UUID.randomUUID();
		this.mineralType = mineralType;
	}
	
	public UUID getUUID() {
		return this.mineralUUID;
	}
	
	public MineralType getMineralType() {
		return this.mineralType;
	}
}
