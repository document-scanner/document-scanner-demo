/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.document.scanner.it.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author richter
 */
@Entity
public class PrimitivesTestClass implements Serializable {
    private static final long serialVersionUID = 1L;
    private int a;
    private Integer b;
    private boolean c;
    private Boolean d;
    private float e;
    private Float f;
    private double g;
    private Double h;
    private byte i;
    private Byte j;
    private long k;
    private Long l;
    private short m;
    private Short n;
    @Id
    private Long id;

    public PrimitivesTestClass() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
