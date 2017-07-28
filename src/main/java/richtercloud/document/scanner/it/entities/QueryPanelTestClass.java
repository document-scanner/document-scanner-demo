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
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author richter
 */
@Entity
public class QueryPanelTestClass implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToOne
    @SuppressWarnings("PMD.UnusedPrivateField")
    private EntityB a;
    @ManyToOne
    @SuppressWarnings("PMD.UnusedPrivateField")
    private EntityB b;
    @OneToMany
    @SuppressWarnings("PMD.UnusedPrivateField")
    private List<EntityB> c;
    @ManyToMany
    @SuppressWarnings("PMD.UnusedPrivateField")
    private List<EntityB> d;
    @Id
    private Long id;

    public QueryPanelTestClass() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
