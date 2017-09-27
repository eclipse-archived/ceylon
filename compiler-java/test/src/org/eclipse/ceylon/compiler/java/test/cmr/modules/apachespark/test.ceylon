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
import java.lang { JString = String, JBoolean = Boolean }
import org.apache.spark { ... }
import org.apache.spark.api.java { ... }
import org.apache.spark.api.java.\ifunction { ... }

shared void run() {
    SparkConf conf = SparkConf().setMaster("local").setAppName("My App");
    JavaSparkContext sc = JavaSparkContext(conf);
    value textFile = sc.textFile("README.md");
    print(textFile.count());
    print(textFile.first());
    // How many lines contain "Ceylon"?
    print(textFile.filter(object satisfies Function<JString,JBoolean> {
            call(JString line) => JBoolean(line.contains(JString("Ceylon")));
        }).count());
}