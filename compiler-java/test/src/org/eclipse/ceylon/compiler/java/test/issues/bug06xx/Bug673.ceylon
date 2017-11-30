/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
@noanno
shared abstract class Bug673_Event() {
}
@noanno
shared abstract class Bug673_GridEvent() extends Bug673_Event() {
}
@noanno
shared abstract class Bug673_StoreEvent() extends Bug673_Event()   {
}
@noanno
shared interface Bug673_EventObserver<Evento>
                    given Evento satisfies Bug673_Event {
    default shared void on( Evento event, Boolean() action ) {
        //Add no mapa
    }
}
@noanno
shared interface Bug673_GridBehavior satisfies Bug673_EventObserver<Bug673_StoreEvent|Bug673_GridEvent> {
    shared actual void on( Bug673_StoreEvent|Bug673_GridEvent event, Boolean() action ) {
        throw;
    }
}
@noanno
shared class Bug673_PaisController() satisfies Bug673_GridBehavior {
}
