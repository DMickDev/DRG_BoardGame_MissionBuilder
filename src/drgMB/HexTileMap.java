package drgMB;

import java.util.HashSet;

import drgMB.HexTile.AdjacentTile;

/**
 * 
 * @author DMickDev
 *
 */
public class HexTileMap {
	/**
	 * 
	 * An enum that holds game presets used for map generation
	 *
	 */
	public enum Preset {
		DEEP_ROCK_GALACTIC (15, 14, 15, 14, 13, 14, 15, 14, 15, 14, 13, 14, 15, 14, 15);
		
		/**
		 * An array of integers representing the length of corresponding rows
		 */
		private final int[] mapRows;
		
		private Preset(int... rows) {
			this.mapRows = rows;
		}
		
		/**
		 * <p>Returns the row at the indicated index if it exists, otherwise it
		 * will return -1. This flag does not currently serve any purpose.</p>
		 * @param i the index of the row to get
		 * @return the row if it exists, otherwise returns -1
		 */
		public int getRow(int i) {
			if (i >= 0 && i < this.mapRows.length) {
				return this.mapRows[i];
			} else {
				return -1;
			}
		}
		
		/**
		 * <p>Returns the array of row lengths in this preset.</p>
		 * @return the array of row lengths
		 */
		public int[] getRows() {
			return mapRows.clone();
		}
		
		/**
		 * <p>Returns the number of rows in this preset.</p>
		 * @return the number of rows in this preset
		 */
		public int getNumRows() {
			return mapRows.length;
		}
	}
	
	/**
	 * A set of all the tiles that make up this map
	 */
	private HashSet<MapTile> mapTiles;
	/**
	 * A set of all the map segments that have been placed on this map
	 */
	private HashSet<MapSegment> mapSegments;
	
	/**
	 * <p>Builds a new map from the given preset.</p>
	 * @param preset the preset to build the map from
	 */
	public HexTileMap(HexTileMap.Preset preset) {
		this.mapSegments = new HashSet<MapSegment>();

		// Create a reference for the first tile in a row
		MapTile firstTile = new MapTile();
		for (int i = 0; i < preset.getNumRows(); i++) {
			// Create a reference for the tile in used in the last iteration of
			// the loop
			MapTile thisTile = firstTile;
			
			// Initialize the loop
			firstTile.setAdjacentTile(new MapTile(), AdjacentTile.RIGHT);
			for (int j = 1; j < preset.getRow(i); j++) {
				// Get the tile to the right of the last tile and set its
				// left reference
				thisTile.getAdjacentTile(AdjacentTile.RIGHT).setAdjacentTile(thisTile, AdjacentTile.LEFT);
				// Traverse right to the next tile
				thisTile = thisTile.getAdjacentTile(AdjacentTile.RIGHT);
				// Add a new tile to the right of this tile
				thisTile.setAdjacentTile(new MapTile(), AdjacentTile.RIGHT);
			}
			
			// If there is another row in the map
			if (i < preset.getNumRows() - 1) {
				// If this row has a greater number of tiles than the next row
				if (preset.getRow(i) > preset.getRow(i + 1)) {
					// Create a tile on the lower right of this tile
					// to begin the next row
					firstTile.setAdjacentTile(new MapTile(), AdjacentTile.LOWER_RIGHT);
					// Move the pointer to the new tile
					firstTile = firstTile.getAdjacentTile(AdjacentTile.LOWER_RIGHT);
				} else {
					// Create a tile on the lower left of this tile
					// to begin the next row
					firstTile.setAdjacentTile(new MapTile(), AdjacentTile.LOWER_LEFT);
					// Move the pointer to the new tile
					firstTile = firstTile.getAdjacentTile(AdjacentTile.LOWER_LEFT);
				}
			}
		}
	}
	
	/**
	 * <p>Invokes the rotate method in all tiles of the given map segment.</p>
	 * @param segment the map segment to rotate
	 * @param byAmount the rotation amount in number of faces
	 */
	public void rotate(MapSegment segment, int byAmount) {
		for (SegmentTile st : segment.getSegmentTiles()) {
			st.rotate(byAmount);
		}
	}
	
	/**
	 * <p>Places the segment to the map one tile at a time with its origin
	 * positioned at the given map tile. Placement will fail if the segment
	 * overlaps another segment or extends beyond the edges of the map.</p>
	 * @param segment the segment to place into the map
	 * @param position the position to place the segment at
	 * @return whether the placement was successful
	 */
	public boolean placeSegment(MapSegment segment, MapTile position) {
		return placeSegmentByTile(segment.getOrigin(), position);
	}
	
	/**
	 * <p>A recursive function that carries out the work of placing map
	 * segments by tile.</p>
	 * @param tile the tile to place
	 * @param position the map position to place the tile at
	 * @return whether the tile placement was successful
	 */
	private boolean placeSegmentByTile(SegmentTile tile, MapTile position) {
		// If position is not out of bounds and does not contain another tile
		if (position != null && position.getSegmentTile() == null) {
			for (int i = 0; i < tile.getNumAdjacentTiles(); i++) {
				// Place this tile, and if unsuccessful, immediately begin
				// exiting recursion
				if (!placeSegmentByTile(tile.getAdjacentTile(i), position.getAdjacentTile(i))) {
					return false;
				}
			}
			// Place the tile if no checks have failed
			position.setSegmentTile(tile);
			// Link the tile to its position
			tile.setAnchorTile(position);
			
			// The placement of this tile has succeeded
			return true;
		}
		// The placement of this tile has failed
		return false;
	}
}
