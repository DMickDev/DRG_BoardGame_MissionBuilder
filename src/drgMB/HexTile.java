package drgMB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * 
 * @author DMickDev
 *
 */
public abstract class HexTile {
	/**
	 * <p></p>
	 * @author DMickDev
	 *
	 */
	public enum AdjacentTile {
		UPPER_RIGHT (0, -1, 1),
		RIGHT (1, -1, 0),
		LOWER_RIGHT (2, -1, -1),
		LOWER_LEFT (3, 1, -1),
		LEFT (4, 1, 0),
		UPPER_LEFT (5, 1, 1);
		
		/**
		 * Refers to the position in the hex tile adjacent tiles array that
		 * corresponds to the adjacent tile in the position indicated by the
		 * enum type name
		 */
		private final int arrayPosition;
		/**
		 * Provides coordinates for the adjacent tile resolving method to use
		 * in identifying the correct tile
		 */
		private final int inverseXTarget;
		private final int inverseYTarget;
		
		/**
		 * <p>Creates a new preset. 
		 * @param arrayPosition the array index corresponding to this adjacency
		 * @param inverseXTarget the inverse of the x coordinate to search for
		 * to resolve this adjacency
		 * @param inverseYTarget the inverse of the y coordinate to search for
		 * to resolve this adjacency
		 */
		private AdjacentTile(int arrayPosition, int inverseXTarget, int inverseYTarget) {
			this.arrayPosition = arrayPosition;
			this.inverseXTarget = inverseXTarget;
			this.inverseYTarget = inverseYTarget;
		}
		
		/**
		 * @return the array index corresponding to this adjacency
		 */
		public int getArrayPosition() {
			return this.arrayPosition;
		}
		
		/**
		 * @return the resolution x coordinate of this adjacency
		 */
		public int getX() {
			return this.inverseXTarget;
		}

		/**
		 * @return the resolution y coordinate of this adjacency
		 */
		public int getY() {
			return this.inverseYTarget;
		}
		
		/**
		 * <p>Finds and returns the enum type that represents the link in the
		 * opposite direction to this one.</p>
		 * @return the opposite adjacency
		 */
		public AdjacentTile getOpposite() {
			return AdjacentTile.values()[(this.getArrayPosition() + 4) % 6];
		}
	}
	
	private static boolean resolveFound = false;
	private static HashSet<UUID> visitedTiles;
	
	private UUID tileUUID;
	private HexTile[] adjacentTiles;
	
	public HexTile() {
		this.tileUUID = UUID.randomUUID();
		this.adjacentTiles = new HexTile[6];
	}
	
	public HexTile(HashMap<HexTile.AdjacentTile, HexTile> adjacentTiles) {
		this.tileUUID = UUID.randomUUID();
		this.adjacentTiles = new HexTile[6];
		for (AdjacentTile aT : adjacentTiles.keySet()) {
			this.adjacentTiles[aT.arrayPosition] = adjacentTiles.get(aT);
		}
	}
	
	public UUID getUUID() {
		return this.tileUUID;
	}
	
	public HexTile[] getAdjacentTiles() {
		HexTile[] returner = new HexTile[this.adjacentTiles.length];
		for (int i = 0; i < this.adjacentTiles.length; i++) {
			returner[i] = this.adjacentTiles[i];
		}
		return returner;
	}
	
	public HexTile getAdjacentTile(int position) {
		if (position < 0 || position >= this.adjacentTiles.length) {
			throw new IllegalArgumentException();
		}
		
		return this.adjacentTiles[position];
	}
	
	public HexTile getAdjacentTile(HexTile.AdjacentTile position) {
		return this.adjacentTiles[position.arrayPosition];
	}
	
	public void setAdjacentTile(HexTile tile, HexTile.AdjacentTile position) {		
		this.adjacentTiles[position.arrayPosition] = tile;
	}
	
	public int getNumAdjacentTiles() {
		return this.adjacentTiles.length;
	}
	
	// Rotate the array linking this tile to its adjacent tiles
	public void rotate(int byAmount) {
		byAmount %= this.adjacentTiles.length;
		HexTile[] rotatedArray = new HexTile[this.adjacentTiles.length];
		
		for (int i = 0; i < this.adjacentTiles.length; i++) {
			if (i < byAmount) {
				rotatedArray[i] = this.getAdjacentTile((this.adjacentTiles.length - byAmount) + i);
			} else {
				rotatedArray[i] = this.getAdjacentTile(i - byAmount);
			}
		}
		
		this.adjacentTiles = rotatedArray;
	}
	
	public void resolveAdjacentTiles() {
		HexTile.visitedTiles = new HashSet<UUID>();
		for (AdjacentTile aT : HexTile.AdjacentTile.values()) {
			this.setAdjacentTile(this.resolveAdjacentTile(this, aT.getX(), aT.getY()), aT);
		}
		HexTile.visitedTiles = null;
	}
	
	private HexTile resolveAdjacentTile(HexTile tile, int x, int y) {
		if (tile != null) {
			HexTile.visitedTiles.add(tile.tileUUID);
		}
		if (x == 0 & y == 0) {
			HexTile.resolveFound = true;
			return tile;
		}
		for (AdjacentTile aT : HexTile.AdjacentTile.values()) {
			// Explore each adjacent tile
			HexTile nextTile = tile.getAdjacentTile(aT.getArrayPosition());
			if (!HexTile.visitedTiles.contains(nextTile.getUUID())) {
				nextTile = this.resolveAdjacentTile(nextTile, x - aT.getX(), y - aT.getY());
				if (HexTile.resolveFound == true) {
					return nextTile;
				}
			}
		}
		return null;
	}
}
