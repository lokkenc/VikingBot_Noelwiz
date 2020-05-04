package ml.model;

/**
 * Enum to classify units based on their weapon range
 */
public enum UnitClassification {
    MELEE {
        public String toString() { return "Melee"; }
    }, RANGED {
        public String toString() { return "Ranged"; }
    }
}
