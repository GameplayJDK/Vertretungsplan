/*
 *     Vertretungsplan Android App
 *     Copyright (C) 2017  GameplayJDK
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.GameplayJDK.Vertretungsplan.Helper;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by GameplayJDK on 10.12.2016.
 */

public final class FragmentHelper {

    private FragmentHelper() {
    }

    public static <T> T findParentFragment(Fragment fragment, Class<T> parentClass, int level, boolean current, boolean acceptActivity) {
        if (parentClass.isInstance(fragment) && current) {
            return (T) fragment;
        } else {
            Fragment parentFragment = fragment.getParentFragment();

            if (parentFragment == null) {
                Activity parentActivity = fragment.getActivity();

                if (parentActivity == null) {
                    return null;
                } else {
                    if (parentClass.isInstance(parentActivity) && acceptActivity) {
                        return (T) parentActivity;
                    } else {
                        return null;
                    }
                }
            } else {
                if (parentClass.isInstance(parentFragment)) {
                    return (T) parentFragment;
                } else {
                    if (level > 0) {
                        level--;

                        return findParentFragment(parentFragment, parentClass, level, false, acceptActivity);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public static <T> T findChildFragment(Fragment fragment, Class<T> childClass, int level, boolean current) {
        if (childClass.isInstance(fragment) && current) {
            return (T) fragment;
        } else {
            List<Fragment> fragmentList = fragment.getChildFragmentManager().getFragments();

            if (fragmentList == null) {
                return null;
            } else {
                for (Fragment childFragment : fragmentList) {
                    if (childClass.isInstance(childFragment)) {
                        return (T) childFragment;
                    } else {
                        if (level > 0) {
                            level--;

                            return findChildFragment(childFragment, childClass, level, false);
                        }
                    }
                }

                return null;
            }
        }
    }

    public static <T> T findSiblingFragment(Fragment fragment, Class<T> childClass, int level, boolean current) {
        if (childClass.isInstance(fragment) && current) {
            return (T) fragment;
        } else {
            Fragment parentFragment = fragment.getParentFragment();

            if (parentFragment == null) {
                return null;
            } else {
                List<Fragment> fragmentList = parentFragment.getFragmentManager().getFragments();

                if (fragmentList == null) {
                    return null;
                } else {
                    for (Fragment childFragment : fragmentList) {
                        if (childClass.isInstance(childFragment)) {
                            return (T) childFragment;
                        } else {
                            if (level > 0) {
                                level--;

                                return findChildFragment(childFragment, childClass, level, false);
                            }
                        }
                    }

                    return null;
                }
            }
        }
    }
}
